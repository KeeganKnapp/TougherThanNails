package dev.keeganknapp.musicsync;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

/**
 * Sends direct, unregistered {@code globalmusic:*} sound events straight to a player's
 * connection, mirroring exactly what vanilla's own {@code /playsound} and {@code /stopsound}
 * commands do internally (verified against the decompiled bytecode of
 * {@code net.minecraft.server.commands.PlaySoundCommand} and {@code StopSoundCommand} in the
 * real {@code com.mojang:minecraft:26.1.2} jar):
 *
 * <ul>
 *   <li>{@code PlaySoundCommand} builds its sound via
 *       {@code Holder.direct(SoundEvent.createVariableRangeEvent(id))} and constructs a
 *       {@code ClientboundSoundPacket} with that holder, which is exactly what's needed for a
 *       dynamic sound id like {@code globalmusic:sweden} that is never registered in the game's
 *       sound-event registry (it only exists in the resource pack's sounds.json).</li>
 *   <li>{@code StopSoundCommand} builds a {@code ClientboundStopSoundPacket(Identifier, SoundSource)}
 *       and sends it via {@code ServerPlayer.connection.send(packet)}.</li>
 * </ul>
 */
final class SoundSender {
    private SoundSender() {
    }

    static void playMusic(ServerPlayer player, String soundId) {
        Identifier id = Identifier.fromNamespaceAndPath("globalmusic", soundId);
        Holder<SoundEvent> sound = Holder.direct(SoundEvent.createVariableRangeEvent(id));
        long seed = player.level().getRandom().nextLong();
        ClientboundSoundPacket packet = new ClientboundSoundPacket(
                sound, SoundSource.MUSIC,
                player.getX(), player.getY(), player.getZ(),
                1.0f, 1.0f, seed);
        player.connection.send(packet);
    }

    static void stopMusic(ServerPlayer player) {
        // stopsound @s music (no specific sound id) - matches "stopsound <target> music" from
        // the original mcfunctions, which stops every currently playing sound in that category.
        ClientboundStopSoundPacket packet = new ClientboundStopSoundPacket(null, SoundSource.MUSIC);
        player.connection.send(packet);
    }
}
