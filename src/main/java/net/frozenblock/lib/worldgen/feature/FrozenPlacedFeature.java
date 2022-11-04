package net.frozenblock.lib.worldgen.feature;

import net.frozenblock.lib.worldgen.feature.util.FrozenPlacementUtils;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.jetbrains.annotations.ApiStatus;

/**
 * Used for easier registry of features.
 * @since 1.1.3
 */

@ApiStatus.Experimental
public class FrozenPlacedFeature {

	private final ResourceKey<PlacedFeature> resourceKey;
	private final ResourceKey<ConfiguredFeature<?, ?>> featureKey;
	private final PlacementModifier[] placementModifiers;


	public FrozenPlacedFeature(ResourceKey<PlacedFeature> resourceKey, ResourceKey<ConfiguredFeature<?, ?>> featureKey, PlacementModifier[] placementModifiers) {
		this.resourceKey = resourceKey;
		this.featureKey = featureKey;
		this.placementModifiers = placementModifiers;
	}

	public ResourceKey<PlacedFeature> getResourceKey() {
		return this.resourceKey;
	}

	public Holder<PlacedFeature> getHolder() {
		return FrozenPlacementUtils.getHolder(this.getResourceKey());
	}

	public ResourceKey<ConfiguredFeature<?, ?>> getFeatureKey() {
		return this.featureKey;
	}

	public PlacementModifier[] getPlacementModifiers() {
		return this.placementModifiers;
	}
}
