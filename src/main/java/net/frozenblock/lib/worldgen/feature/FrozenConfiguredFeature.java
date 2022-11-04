package net.frozenblock.lib.worldgen.feature;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.ApiStatus;

/**
 * Used for easier registry of features.
 * @since 1.1.3
 */

@ApiStatus.Experimental
public class FrozenConfiguredFeature<FC extends FeatureConfiguration, F extends FeatureConfiguration> {

	public final ResourceKey<ConfiguredFeature<?, ?>> resourceKey;
	public final Feature<F> feature;
	public final FC featureConfiguration;


	public FrozenConfiguredFeature(ResourceKey<ConfiguredFeature<?, ?>> resourceKey, Feature<F> feature, FC featureConfiguration) {
		this.resourceKey = resourceKey;
		this.feature = feature;
		this.featureConfiguration = featureConfiguration;
	}
}
