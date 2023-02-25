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

package net.frozenblock.lib.testmod.config;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.config.api.client.ClientConfig;
import net.frozenblock.lib.config.api.client.option.Option;
import net.frozenblock.lib.config.api.entry.Exclude;
import net.frozenblock.lib.config.api.entry.TypedEntry;
import net.frozenblock.lib.config.api.entry.TypedEntryType;
import net.frozenblock.lib.config.api.instance.Config;
import net.frozenblock.lib.config.api.instance.gson.GsonConfig;
import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import net.frozenblock.lib.testmod.FrozenTestMain;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import java.util.List;

public class TestConfig {

	// Make sure to define TypedEntryTypes at top of class.
	public static final TypedEntryType<SoundEvent> SOUND_EVENT = ConfigRegistry.register(
			new TypedEntryType<>(
					FrozenTestMain.MOD_ID,
					SoundEvent.CODEC
			)
	);

	public static final TypedEntryType<List<Vec3>> VEC3_LIST = ConfigRegistry.register(
			new TypedEntryType<>(
					FrozenTestMain.MOD_ID,
					Codec.list(Vec3.CODEC)
			)
	);

	@Exclude
	private static final Config<TestConfig> INSTANCE = ConfigRegistry.register(
			new GsonConfig<>(FrozenTestMain.MOD_ID, TestConfig.class)
	);

	public boolean testToggle = true;

	public int testInt = 69;

	public long testLong = 69420L;

	public float testFloat = 69.420F;

	public double testDouble = 69.4206942069420D;

	public List<Integer> testIntList = List.of(45);

	public TypedEntry<Boolean> typedBoolean = new TypedEntry<>(
			TypedEntryType.BOOLEAN, true
	);

	public TypedEntry<Integer> typedInt = new TypedEntry<>(
			TypedEntryType.INTEGER, 69
	);

	public TypedEntry<List<Double>> typedDoubleList = new TypedEntry<>(
			TypedEntryType.DOUBLE_LIST, List.of(1D, 2D, 69.69696969696969696969696969420D)
	);

	public TypedEntry<SoundEvent> randomSound = new TypedEntry<>(
			SOUND_EVENT, SoundEvents.BEE_LOOP
	);

	public TypedEntry<List<Vec3>> typedVecList = new TypedEntry<>(
			VEC3_LIST, List.of(new Vec3(0, 0, 0), new Vec3(1, 1, 1))
	);

	@Exclude
	public boolean notABoolean = false;

	@Exclude
	public int notAnInt = 0;

	public static Config<TestConfig> get() {
		return INSTANCE;
	}

	@Environment(EnvType.CLIENT)
	public Screen makeGui(Screen parent) {
		return ClientConfig.makeBuilder(INSTANCE)
				.title(Component.literal("FrozenLib Testmod"))
				.option(
						Option.createBoolean(
								Component.literal("Test Toggle"),
								Option.cachedConstantTooltip(Component.translatable("Test Toggle Tooltip")),
								true,
								newValue -> {
									FrozenMain.log("Received new value from save", FrozenMain.UNSTABLE_LOGGING);
									this.testToggle = newValue;
								}
						)
				)
				.option(
						new Option<>(
								Component.literal("Test Double"),
								Option.cachedConstantTooltip(Component.translatable("Test Double Tooltip")),
								Option::percentValueLabel,
								new Option.IntRange(0, 100).xmap(val -> (double) val, Double::intValue),
								Codec.doubleRange(0.0, 1.0),
								this.testDouble,
								value -> this.testDouble = value
						)
				)
				.option(
						Option.createIntSlider(
								Component.literal("Test Int"),
								Option.cachedConstantTooltip(Component.translatable("Test Int Tooltip")),
								0,
								100,
								this.testInt,
								value -> this.testInt = value
						)
				)
				.option(
						new Option<>(
								Component.literal("Test Typed Bool"),
								Option.cachedConstantTooltip(Component.translatable("Test Typed Bool Tooltip")),
								(caption1, value) -> value ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF,
								Option.get(this.typedBoolean.value()),
								this.typedBoolean.value(),
								value -> this.typedBoolean = new TypedEntry<>(this.typedBoolean.type(), value)
						)
				)
				.save(INSTANCE::save)
				.build()
				.makeScreen(parent);
	}
}
