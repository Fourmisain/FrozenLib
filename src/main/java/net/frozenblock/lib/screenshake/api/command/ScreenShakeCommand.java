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

package net.frozenblock.lib.screenshake.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.frozenblock.lib.networking.FrozenNetworking;
import net.frozenblock.lib.screenshake.api.ScreenShakeManager;
import net.frozenblock.lib.screenshake.impl.EntityScreenShakeInterface;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ScreenShakeCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder = Commands.literal("screenshake").requires(source -> source.hasPermission(2));

		literalArgumentBuilder.then(Commands.argument("pos", Vec3Argument.vec3()).executes(context -> shake(context.getSource(), Vec3Argument.getVec3(context, "pos"), 1F, 10, 5, 16F))
						.then(Commands.argument("intensity", FloatArgumentType.floatArg()).executes(context -> shake(context.getSource(), Vec3Argument.getVec3(context, "pos"), FloatArgumentType.getFloat(context, "intensity"), 10, 5, 16F))
								.then(Commands.argument("duration", IntegerArgumentType.integer()).executes(context -> shake(context.getSource(), Vec3Argument.getVec3(context, "pos"), FloatArgumentType.getFloat(context, "intensity"), IntegerArgumentType.getInteger(context, "duration"), 5, 16F))
										.then(Commands.argument("durationFalloffStart", IntegerArgumentType.integer()).executes(context -> shake(context.getSource(), Vec3Argument.getVec3(context, "pos"), FloatArgumentType.getFloat(context, "intensity"), IntegerArgumentType.getInteger(context, "duration"), IntegerArgumentType.getInteger(context, "durationFalloffStart"), 16F))
												.then(Commands.argument("maxDistance", FloatArgumentType.floatArg()).executes(context -> shake(context.getSource(), Vec3Argument.getVec3(context, "pos"), FloatArgumentType.getFloat(context, "intensity"), IntegerArgumentType.getInteger(context, "duration"), IntegerArgumentType.getInteger(context, "durationFalloffStart"), FloatArgumentType.getFloat(context, "maxDistance")))
														.then(Commands.argument("players", EntityArgument.players()).executes(context -> shake(context.getSource(), Vec3Argument.getVec3(context, "pos"), FloatArgumentType.getFloat(context, "intensity"), IntegerArgumentType.getInteger(context, "duration"), IntegerArgumentType.getInteger(context, "durationFalloffStart"), FloatArgumentType.getFloat(context, "maxDistance"), EntityArgument.getPlayers(context, "players")))))))));

		literalArgumentBuilder.then(Commands.argument("entity", EntityArgument.entities()).executes(context -> shake(context.getSource(), EntityArgument.getEntities(context, "entity"), 1F, 10, 5, 16F))
						.then(Commands.argument("intensity", FloatArgumentType.floatArg()).executes(context -> shake(context.getSource(), EntityArgument.getEntities(context, "entity"), FloatArgumentType.getFloat(context, "intensity"), 10, 5, 16F))
								.then(Commands.argument("duration", IntegerArgumentType.integer()).executes(context -> shake(context.getSource(), EntityArgument.getEntities(context, "entity"), FloatArgumentType.getFloat(context, "intensity"), IntegerArgumentType.getInteger(context, "duration"), 5, 16F))
										.then(Commands.argument("durationFalloffStart", IntegerArgumentType.integer()).executes(context -> shake(context.getSource(), EntityArgument.getEntities(context, "entity"), FloatArgumentType.getFloat(context, "intensity"), IntegerArgumentType.getInteger(context, "duration"), IntegerArgumentType.getInteger(context, "durationFalloffStart"), 16F))
												.then(Commands.argument("maxDistance", FloatArgumentType.floatArg()).executes(context -> shake(context.getSource(), EntityArgument.getEntities(context, "entity"), FloatArgumentType.getFloat(context, "intensity"), IntegerArgumentType.getInteger(context, "duration"), IntegerArgumentType.getInteger(context, "durationFalloffStart"), FloatArgumentType.getFloat(context, "maxDistance"))))))));

		literalArgumentBuilder.then(Commands.literal("remove")
				.then(Commands.literal("for").then(Commands.argument("players", EntityArgument.players()).executes(context -> removeShakesFor(context.getSource(), EntityArgument.getPlayers(context, "players")))))
				.then(Commands.literal("from").then(Commands.argument("entities", EntityArgument.entities()).executes(context -> removeShakesFrom(context.getSource(), EntityArgument.getEntities(context, "entity")))))
		);

		dispatcher.register(literalArgumentBuilder);
	}

	private static int shake(CommandSourceStack source, Vec3 vec3, float intensity, int duration, int durationFalloffStart, float maxDistance) {
		vec3 = new Vec3(Math.round(vec3.x()), Math.round(vec3.y()), Math.round(vec3.z()));
		ScreenShakeManager.addScreenShake(source.getLevel(), intensity, duration, durationFalloffStart, vec3.x(), vec3.y(), vec3.z(), maxDistance);
		Vec3 finalVec = vec3;
		source.sendSuccess(() -> Component.translatable("commands.screenshake.success", finalVec.x(), finalVec.y(), finalVec.z(), intensity, duration, durationFalloffStart, maxDistance), true);
		return 1;
	}

	private static int shake(CommandSourceStack source, Vec3 vec3, float intensity, int duration, int durationFalloffStart, float maxDistance, Collection<? extends ServerPlayer> entities) {
		vec3 = new Vec3(Math.round(vec3.x()), Math.round(vec3.y()), Math.round(vec3.z()));
		FriendlyByteBuf screenShakeByteBuf = ScreenShakeManager.createScreenShakeByteBuf(intensity, duration, durationFalloffStart, vec3.x(), vec3.y(), vec3.z(), maxDistance, 0);
		StringBuilder playerString = new StringBuilder();
		for (ServerPlayer serverPlayer : entities) {
			ServerPlayNetworking.send(serverPlayer, FrozenNetworking.SCREEN_SHAKE_PACKET, screenShakeByteBuf);
			playerString.append(serverPlayer.getDisplayName().getString()).append(", ");
		}
		Vec3 finalVec = vec3;
		source.sendSuccess(() -> Component.translatable("commands.screenshake.player.success", playerString.toString(), finalVec.x(), finalVec.y(), finalVec.z(), intensity, duration, durationFalloffStart, maxDistance), true);

		return 1;
	}

	private static int shake(CommandSourceStack source, Collection<? extends Entity> entities, float intensity, int duration, int durationFalloffStart, float maxDistance) {
		StringBuilder entityString = new StringBuilder();
		for (Entity entity : entities) {
			ScreenShakeManager.addEntityScreenShake(entity, intensity, duration, durationFalloffStart, maxDistance);
			entityString.append(entity.getDisplayName().getString()).append(", ");
		}
		source.sendSuccess(() -> Component.translatable("commands.screenshake.entity.success", entityString.toString(), intensity, duration, durationFalloffStart, maxDistance), true);
		return 1;
	}

	private static int removeShakesFor(CommandSourceStack source, Collection<? extends ServerPlayer> entities) {
		StringBuilder playerString = new StringBuilder();
		boolean onePlayer = entities.size() == 1;
		FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
		for (ServerPlayer serverPlayer : entities) {
			ServerPlayNetworking.send(serverPlayer, FrozenNetworking.REMOVE_SCREEN_SHAKES_PACKET, friendlyByteBuf);
			playerString.append(serverPlayer.getDisplayName().getString()).append(onePlayer ? "" : ", ");
		}
		source.sendSuccess(() -> Component.translatable(onePlayer ? "commands.screenshake.remove.player.success" : "commands.screenshake.remove.player.success.multiple", playerString.toString()), true);
		return 1;
	}

	private static int removeShakesFrom(CommandSourceStack source, Collection<? extends Entity> entities) {
		StringBuilder entityString = new StringBuilder();
		int entityAmount = 0;
		List<Entity> affectedEntities = new ArrayList<>();
		for (Entity entity : entities) {
			if (!((EntityScreenShakeInterface)entity).getScreenShakeManager().getShakes().isEmpty()) {
				FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
				friendlyByteBuf.writeVarInt(entity.getId());
				affectedEntities.add(entity);
				((EntityScreenShakeInterface)entity).getScreenShakeManager().getShakes().clear();
				for (ServerPlayer serverPlayer : PlayerLookup.tracking(source.getLevel(), entity.blockPosition())) {
					ServerPlayNetworking.send(serverPlayer, FrozenNetworking.REMOVE_ENTITY_SCREEN_SHAKES_PACKET, friendlyByteBuf);
				}
				entityAmount += 1;
			}
		}

		boolean oneEntity = affectedEntities.size() == 1;
		for (Entity entity : affectedEntities) {
			entityString.append(entity.getDisplayName().getString()).append(oneEntity ? "" : ", ");
		}

		if (entityAmount > 0) {
			source.sendSuccess(() -> Component.translatable(oneEntity ? "commands.screenshake.remove.entity.success" : "commands.screenshake.remove.entity.success.multiple", entityString.toString()), true);
			return 1;
		} else {
			source.sendFailure(Component.translatable("commands.screenshake.remove.entity.failure"));
			return 0;
		}
	}

}
