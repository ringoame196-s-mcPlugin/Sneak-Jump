package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.message.MessageKey
import com.github.ringoame196_s_mcPlugin.message.MessageManager
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Event

interface JumpBoots : JumpItem {
    val bootsColor: Color
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
}
