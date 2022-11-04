package net.frozenblock.lib.worldgen.feature.util;

import java.util.List;
import net.frozenblock.lib.worldgen.feature.FrozenConfiguredFeature;
import net.frozenblock.lib.worldgen.feature.FrozenPlacedFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

public class FrozenPlacementUtils {

	public static ResourceKey<PlacedFeature> createKey(String namespace, String path) {
		return ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(namespace, path));
	}

	public static void register(BootstapContext<PlacedFeature> bootstapContext, ResourceKey<PlacedFeature> registryKey, Holder<ConfiguredFeature<?, ?>> holder, List<PlacementModifier> list) {
		PlacementUtils.register(bootstapContext, registryKey, holder, list);
	}

	public static void register(BootstapContext<PlacedFeature> bootstapContext, ResourceKey<PlacedFeature> registryKey, Holder<ConfiguredFeature<?, ?>> holder, PlacementModifier... placementModifiers) {
		PlacementUtils.register(bootstapContext, registryKey, holder, placementModifiers);
	}

	public static FrozenPlacedFeature placedFeature(String namespace, String path, FrozenConfiguredFeature feature, PlacementModifier... placementModifiers) {
		return new FrozenPlacedFeature(createKey(namespace, path), feature.getResourceKey(), placementModifiers);
	}

	public static Holder<PlacedFeature> getHolder(ResourceKey<PlacedFeature> resourceKey) {
		return VanillaRegistries.createLookup().lookupOrThrow(Registry.PLACED_FEATURE_REGISTRY).getOrThrow(resourceKey);
	}
}
