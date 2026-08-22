import generator.ReadMeGenerator
import generator.ResourcesGenerator
import generator.SetupContext
import generator.SrcGenerator
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.RefAlreadyExistsException
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import update.BuildGradleUpdater
import java.io.IOException

open class SetupTask : DefaultTask() {
    @TaskAction
    fun action() {
        val projectDir = project.projectDir
        val sentinelFile = projectDir.resolve(".setup_completed")
        val isForce = project.hasProperty("force")

        // すでに実行済みで、-Pforce が付与されていない場合は中断
        if (sentinelFile.exists() && !isForce) {
            logger.warn("⚠️ Setup is already complete.")
            logger.warn("If you want to run it again, specify ‘./gradlew setup -Pforce’ or delete the .setup_completed file.")
            return
        }

        openGit().use { git ->
            setupBranch(git)

            val ctx  = makeSetupContext(git)
            setupGitHubTopics(ctx)
            SrcGenerator(projectDir, ctx).generate()
            ResourcesGenerator(projectDir).generate()
            BuildGradleUpdater(projectDir, ctx).update()
            ReadMeGenerator(projectDir,ctx).generate()
        }
        // セットアップ完了の目印を作成
        sentinelFile.writeText("Setup completed at: ${java.time.Instant.now()}")
        logger.lifecycle("✅ Setup is complete (Created .setup_completed)")
    }

    private fun openGit(): Git {
        val projectDir = project.projectDir
        val repository = try {
            FileRepositoryBuilder()
                .setGitDir(projectDir.resolve(".git"))
                .readEnvironment()
                .findGitDir()
                .build()
        } catch (ex: IOException) {
            error("リポジトリが見つかりませんでした")
        }
        return Git(repository)
    }

    private fun makeSetupContext(git: Git):SetupContext {
        val remoteList = git.remoteList().call()
        val uri = remoteList.flatMap { it.urIs }.firstOrNull { it.host == "github.com" }
            ?: error("GitHub のプッシュ先が見つかりませんでした")

        val path = uri.path

        val segments = path.trim('/').split('/')
        require(segments.size >= 2) { "GitHub URL が不正です: $path" }

        val rawAccount = segments[0]
        val account = rawAccount.replace('-', '_')
        val groupId = "com.github.$account"

        return SetupContext(
            rawAccount,
            account,
            groupId,
            "src/main/kotlin/com/github/$account",
            project.name,
            project.findProperty("mcVersion").toString(),
            path
        )
    }

    private fun setupBranch(git: Git) {
        try {
            git.checkout()
                .setName("developer")
                .setCreateBranch(true)
                .call()
            logger.lifecycle("🌱 developer ブランチを作成＆切替")
        } catch (e: RefAlreadyExistsException) {
            git.checkout().setName("developer").call()
            logger.lifecycle("🔁 developer ブランチへ切替")
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun setupGitHubTopics(ctx: SetupContext) {
        try {
            ProcessBuilder(
                "gh",
                "repo",
                "edit",
                "${ctx.rawAccount}/${ctx.projectName}",
                "--add-topic", "minecraft",
                "--add-topic", "minecraft-plugin",
                "--add-topic", "paper",
                "--add-topic", "papermc",
                "--add-topic", "kotlin",
                "--add-topic", ctx.projectName.lowercase().replace("_", "-")
            )
                .inheritIO()
                .start()
                .waitFor()

            logger.lifecycle("🏷 GitHub Topics を設定")
        } catch (e: Exception) {
            logger.warn("GitHub Topics の設定に失敗: ${e.message}")
        }
    }
}
