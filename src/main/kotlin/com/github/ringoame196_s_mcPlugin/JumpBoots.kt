package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.message.MessageKey
import com.github.ringoame196_s_mcPlugin.message.MessageManager
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Color
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.meta.Damageable

interface JumpBoots : JumpItem {
    val bootsColor: Color
    val durabilityCost: Int

    val jumpParticle: Particle
    val jumpParticleCount: Int get() = 15

    val jumpSound: Sound
    val jumpSoundVolume: Float get() = 1.0f
    val jumpSoundPitch: Float get() = 1.0f

    fun jump(
        player: Player
    )

    fun playJumpEffect(player: Player) {
        val world = player.world
        val location = player.location
        val particle = this.jumpParticle
        val particleCount = this.jumpParticleCount
        val sound = this.jumpSound
        val volume = this.jumpSoundVolume
        val pitch = this.jumpSoundPitch
        world.spawnParticle(particle, location, particleCount, 0.2, 0.1, 0.2, 0.05)
        player.playSound(player, sound, volume, pitch)
    }

    fun sendJumpSuccess(player: Player, messageManager: MessageManager) {
        val message = messageManager.get(MessageKey.JUMP_MESSAGE)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }

    fun sendJumpCancel(player: Player, messageManager: MessageManager) {
        val message = messageManager.get(MessageKey.NO_CAN_JUMP_MESSAGE)
        val sound = Sound.BLOCK_NOTE_BLOCK_BELL
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
        player.playSound(player, sound, 1f, 1f)
    }

    fun cancel(player: Player) {}

    fun isAction(player: Player, e: Event? = null): Boolean = true
    fun isCancel(player: Player, e: Event? = null): Boolean = false

    fun reduceDurability(player: Player, amount: Int = durabilityCost) {
        if (player.gameMode == GameMode.CREATIVE) return
        if (amount == 0) return

        val boots = player.inventory.boots ?: return
        val meta = boots.itemMeta as? Damageable ?: return

        val currentDamage = meta.damage
        val maxDurability = boots.type.maxDurability

        // 耐久値オーバーで破壊されるか判定
        if (currentDamage + amount >= maxDurability) {
            // ブーツを破壊（壊れる音とエフェクトを再生）
            player.inventory.boots = null
            player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
            player.spawnParticle(Particle.ITEM_CRACK, player.location.add(0.0, 1.0, 0.0), 10, boots)
        } else {
            // 耐久値を減らして更新
            meta.damage = currentDamage + amount
            boots.itemMeta = meta
        }
    }
}
