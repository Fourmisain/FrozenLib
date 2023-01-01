/*
 * Copyright 2022 FrozenBlock
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

package net.frozenblock.lib.worldgen.surface.mixin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.frozenblock.lib.worldgen.surface.api.entrypoint.FrozenLiveSurfaceRuleEntrypoint;
import net.frozenblock.lib.worldgen.surface.api.entrypoint.FrozenSurfaceRuleEntrypoint;
import net.frozenblock.lib.worldgen.surface.impl.SetNoiseGeneratorPresetInterface;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseGeneratorSettings.class)
public class NoiseGeneratorSettingsMixin implements SetNoiseGeneratorPresetInterface {

	@Shadow
	@Mutable
	private SurfaceRules.RuleSource surfaceRule;

	@Unique
	private ResourceLocation preset;
	@Unique
	private boolean hasCheckedEntrypoints;
	@Unique
	private SurfaceRules.RuleSource heldSurfaceRule;
	@Unique
	private static final List<EntrypointContainer<FrozenLiveSurfaceRuleEntrypoint>> liveEntrypoints = FabricLoader.getInstance().getEntrypointContainers("frozenlib:live_surfacerules", FrozenLiveSurfaceRuleEntrypoint.class);

	@Inject(method = "surfaceRule", at = @At("HEAD"), cancellable = true)
	private void surfaceRule(CallbackInfoReturnable<SurfaceRules.RuleSource> cir) {
		if (!this.hasCheckedEntrypoints) {
			Map<SurfaceRules.RuleSource, ResourceLocation> sourceHolder = new LinkedHashMap<>();

			FabricLoader.getInstance().getEntrypointContainers("frozenlib:surfacerules", FrozenSurfaceRuleEntrypoint.class).forEach(entrypoint -> {
				try {
					FrozenSurfaceRuleEntrypoint ruleEntrypoint = entrypoint.getEntrypoint();
					ruleEntrypoint.addRuleSources(sourceHolder);
				} catch (Throwable ignored) {

				}
			});

			SurfaceRules.RuleSource newSource = null;
			for (SurfaceRules.RuleSource ruleSource : sourceHolder.keySet()) {
				if (sourceHolder.get(ruleSource).equals(this.preset)) {
					if (newSource == null) {
						newSource = ruleSource;
					} else {
						newSource = SurfaceRules.sequence(newSource, ruleSource);
					}
				}
			}
			this.hasCheckedEntrypoints = true;

			if (newSource != null) {
				this.surfaceRule = SurfaceRules.sequence(newSource, this.surfaceRule, newSource);
			}
			this.heldSurfaceRule = this.surfaceRule;
		}

		Map<SurfaceRules.RuleSource, ResourceLocation> sourceHolder = new LinkedHashMap<>();

		liveEntrypoints.forEach(entrypoint -> {
			try {
				FrozenLiveSurfaceRuleEntrypoint ruleEntrypoint = entrypoint.getEntrypoint();
				ruleEntrypoint.addLiveRuleSources(sourceHolder);
			} catch (Throwable ignored) {

			}
		});

		SurfaceRules.RuleSource newSource = null;
		if (!sourceHolder.isEmpty()) {
			for (SurfaceRules.RuleSource ruleSource : sourceHolder.keySet()) {
				if (sourceHolder.get(ruleSource).equals(this.preset)) {
					if (newSource == null) {
						newSource = ruleSource;
					} else {
						newSource = SurfaceRules.sequence(newSource, ruleSource);
					}
				}
			}

			if (newSource != null) {
				this.surfaceRule = SurfaceRules.sequence(newSource, this.heldSurfaceRule, newSource);
			} else {
				this.surfaceRule = this.heldSurfaceRule;
			}
		} else {
			this.surfaceRule = this.heldSurfaceRule;
		}
	}

	@Inject(method = "register", at = @At("HEAD"))
	private static void register(Registry<NoiseGeneratorSettings> registry, ResourceKey<NoiseGeneratorSettings> key, NoiseGeneratorSettings settings, CallbackInfoReturnable<Holder<NoiseGeneratorSettings>> info) {
		SetNoiseGeneratorPresetInterface.class.cast(settings).setPreset(key.location());
	}

	@Override
	public void setPreset(ResourceLocation location) {
		this.preset = location;
	}
}
