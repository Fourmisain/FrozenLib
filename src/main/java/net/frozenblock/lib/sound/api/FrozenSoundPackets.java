/*
 * Copyright 2023 FrozenBlock
 * This file is part of FrozenLib.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.sound.api;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.frozenblock.lib.networking.FrozenNetworking;
import net.frozenblock.lib.sound.api.networking.FlyBySoundPacket;
import net.frozenblock.lib.sound.api.networking.LocalPlayerSoundPacket;
import net.frozenblock.lib.sound.api.networking.LocalSoundPacket;
import net.frozenblock.lib.sound.api.networking.MovingRestrictionSoundPacket;
import net.frozenblock.lib.sound.api.networking.StartingMovingRestrictionSoundLoopPacket;
import net.frozenblock.lib.sound.impl.EntityLoopingFadingDistanceSoundInterface;
import net.frozenblock.lib.sound.impl.EntityLoopingSoundInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public final class FrozenSoundPackets {
	private FrozenSoundPackets() {
		throw new UnsupportedOperationException("FrozenSoundPackets contains only static declarations.");
	}

	public static void createLocalSound(@NotNull Level level, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {
		if (!level.isClientSide) {
			var packet = new LocalSoundPacket(pos.getX(), pos.getY(), pos.getZ(), sound, source, volume, pitch, distanceDelay);
			for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) level, pos)) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	public static void createLocalSound(@NotNull Level level, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {
		if (!level.isClientSide) {
			var packet = new LocalSoundPacket(x, y, z, sound, source, volume, pitch, distanceDelay);
			for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) level, BlockPos.containing(x, y, z))) {
				ServerPlayNetworking.send(player, packet);
			}
		}
	}

	public static void createFlybySound(@NotNull Level world, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch) {
		if (!world.isClientSide) {
			var packet = new FlyBySoundPacket(entity.getId(), sound, category, volume, pitch);
			for (ServerPlayer player : PlayerLookup.tracking(entity)) {
				ServerPlayNetworking.send(player, packet);
			}
			if (entity instanceof ServerPlayer serverPlayer) {
				ServerPlayNetworking.send(serverPlayer, packet);
			}
		}
	}

    public static void createMovingRestrictionSound(@NotNull Level world, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation predicate, boolean stopOnDeath) {
        if (!world.isClientSide) {
            var packet = new MovingRestrictionSoundPacket(entity.getId(), sound, category, volume, pitch, predicate, stopOnDeath, false);
            for (ServerPlayer player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, packet);
            }
			if (entity instanceof ServerPlayer serverPlayer) {
				ServerPlayNetworking.send(serverPlayer, packet);
			}
        }
    }

    public static void createMovingRestrictionLoopingSound(@NotNull Level world, Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation predicate, boolean stopOnDeath) {
        if (!world.isClientSide) {
			var packet = new MovingRestrictionSoundPacket(entity.getId(), sound, category, volume, pitch, predicate, stopOnDeath, true);
            for (ServerPlayer player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, packet);
            }
			if (entity instanceof ServerPlayer serverPlayer) {
				ServerPlayNetworking.send(serverPlayer, packet);
			}
			((EntityLoopingSoundInterface)entity).addSound(BuiltInRegistries.SOUND_EVENT.getKey(sound), category, volume, pitch, predicate, stopOnDeath);
        }
    }

    public static void createMovingRestrictionLoopingSound(ServerPlayer player, @NotNull Entity entity, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation id, boolean stopOnDeath) {
        var packet = new MovingRestrictionSoundPacket(entity.getId(), sound, category, volume, pitch, id, stopOnDeath, true);
		ServerPlayNetworking.send(player, packet);
    }

    public static void createMovingRestrictionLoopingFadingDistanceSound(@NotNull Level world, Entity entity, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, ResourceLocation predicate, boolean stopOnDeath, float fadeDist, float maxDist) {
        if (!world.isClientSide) {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeVarInt(entity.getId());
            byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound);
            byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound2);
            byteBuf.writeEnum(category);
            byteBuf.writeFloat(volume);
            byteBuf.writeFloat(pitch);
            byteBuf.writeFloat(fadeDist);
            byteBuf.writeFloat(maxDist);
            byteBuf.writeResourceLocation(predicate);
			byteBuf.writeBoolean(stopOnDeath);
            for (ServerPlayer player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, FrozenNetworking.MOVING_RESTRICTION_LOOPING_FADING_DISTANCE_SOUND_PACKET, byteBuf);
            }
			if (entity instanceof ServerPlayer serverPlayer) {
				ServerPlayNetworking.send(serverPlayer, FrozenNetworking.MOVING_RESTRICTION_LOOPING_FADING_DISTANCE_SOUND_PACKET, byteBuf);
			}
			((EntityLoopingFadingDistanceSoundInterface)entity).addFadingDistanceSound(BuiltInRegistries.SOUND_EVENT.getKey(sound), BuiltInRegistries.SOUND_EVENT.getKey(sound2), category, volume, pitch, predicate, stopOnDeath, fadeDist, maxDist);
        }
    }

    public static void createMovingRestrictionLoopingFadingDistanceSound(ServerPlayer player, @NotNull Entity entity, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, ResourceLocation predicate, boolean stopOnDeath, float fadeDist, float maxDist) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entity.getId());
        byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound);
        byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound2);
        byteBuf.writeEnum(category);
        byteBuf.writeFloat(volume);
        byteBuf.writeFloat(pitch);
        byteBuf.writeFloat(fadeDist);
        byteBuf.writeFloat(maxDist);
        byteBuf.writeResourceLocation(predicate);
		byteBuf.writeBoolean(stopOnDeath);
        ServerPlayNetworking.send(player, FrozenNetworking.MOVING_RESTRICTION_LOOPING_FADING_DISTANCE_SOUND_PACKET, byteBuf);
    }

    public static void createMovingRestrictionFadingDistanceSound(ServerPlayer player, @NotNull Entity entity, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, ResourceLocation predicate, boolean stopOnDeath, float fadeDist, float maxDist) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entity.getId());
        byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound);
        byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound2);
        byteBuf.writeEnum(category);
        byteBuf.writeFloat(volume);
        byteBuf.writeFloat(pitch);
        byteBuf.writeFloat(fadeDist);
        byteBuf.writeFloat(maxDist);
        byteBuf.writeResourceLocation(predicate);
		byteBuf.writeBoolean(stopOnDeath);
        ServerPlayNetworking.send(player, FrozenNetworking.MOVING_FADING_DISTANCE_SOUND_PACKET, byteBuf);
    }

    public static void createFadingDistanceSound(@NotNull Level world, Vector3d pos, SoundEvent sound, SoundEvent sound2, SoundSource category, float volume, float pitch, ResourceLocation predicate, boolean stopOnDeath, float fadeDist, float maxDist) {
        if (!world.isClientSide) {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeDouble(pos.x);
            byteBuf.writeDouble(pos.y);
            byteBuf.writeDouble(pos.z);
            byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound);
            byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound2);
            byteBuf.writeEnum(category);
            byteBuf.writeFloat(volume);
            byteBuf.writeFloat(pitch);
            byteBuf.writeFloat(fadeDist);
            byteBuf.writeFloat(maxDist);
            byteBuf.writeResourceLocation(predicate);
			byteBuf.writeBoolean(stopOnDeath);
            for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) world, BlockPos.containing(pos.x, pos.y, pos.z))) {
                ServerPlayNetworking.send(player, FrozenNetworking.FADING_DISTANCE_SOUND_PACKET, byteBuf);
            }
        }
    }

    public static void createStartingMovingRestrictionLoopingSound(@NotNull Level world, Entity entity, SoundEvent startingSound, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation predicate, boolean stopOnDeath) {
        if (!world.isClientSide) {
            var packet = new StartingMovingRestrictionSoundLoopPacket(entity.getId(), startingSound, sound, category, volume, pitch, predicate, stopOnDeath);
            for (ServerPlayer player : PlayerLookup.tracking(entity)) {
                ServerPlayNetworking.send(player, packet);
            }
			if (entity instanceof ServerPlayer serverPlayer) {
				ServerPlayNetworking.send(serverPlayer, packet);
			}
			((EntityLoopingSoundInterface)entity).addSound(BuiltInRegistries.SOUND_EVENT.getKey(sound), category, volume, pitch, predicate, stopOnDeath);
        }
    }

    public static void createStartingMovingRestrictionLoopingSound(ServerPlayer player, @NotNull Entity entity, SoundEvent startingSound, SoundEvent sound, SoundSource category, float volume, float pitch, ResourceLocation predicate, boolean stopOnDeath) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeVarInt(entity.getId());
        byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, startingSound);
        byteBuf.writeId(BuiltInRegistries.SOUND_EVENT, sound);
        byteBuf.writeEnum(category);
        byteBuf.writeFloat(volume);
        byteBuf.writeFloat(pitch);
        byteBuf.writeResourceLocation(predicate);
		byteBuf.writeBoolean(stopOnDeath);
        ServerPlayNetworking.send(player, FrozenNetworking.STARTING_RESTRICTION_LOOPING_SOUND_PACKET, byteBuf);
    }

	public static void createLocalPlayerSound(ServerPlayer player, SoundEvent sound, float volume, float pitch) {
		var packet = new LocalPlayerSoundPacket(sound, volume, pitch);
		ServerPlayNetworking.send(player, packet);
	}

}
