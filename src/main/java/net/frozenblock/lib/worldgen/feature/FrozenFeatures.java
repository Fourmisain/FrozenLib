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

package net.frozenblock.lib.worldgen.feature;

import net.frozenblock.lib.FrozenMain;
import net.frozenblock.lib.worldgen.feature.features.CircularWaterloggedVegetationPatchFeature;
import net.frozenblock.lib.worldgen.feature.features.ColumnWithDiskFeature;
import net.frozenblock.lib.worldgen.feature.features.DownwardsPillarFeature;
import net.frozenblock.lib.worldgen.feature.features.FadingDiskCarpetFeature;
import net.frozenblock.lib.worldgen.feature.features.FadingDiskFeature;
import net.frozenblock.lib.worldgen.feature.features.FadingDiskTagExceptInBiomeFeature;
import net.frozenblock.lib.worldgen.feature.features.FadingDiskTagFeature;
import net.frozenblock.lib.worldgen.feature.features.NoisePathFeature;
import net.frozenblock.lib.worldgen.feature.features.NoisePathSwapUnderWaterFeature;
import net.frozenblock.lib.worldgen.feature.features.NoisePathSwapUnderWaterTagFeature;
import net.frozenblock.lib.worldgen.feature.features.NoisePathTagFeature;
import net.frozenblock.lib.worldgen.feature.features.NoisePathTagUnderWaterFeature;
import net.frozenblock.lib.worldgen.feature.features.NoisePathUnderWaterFeature;
import net.frozenblock.lib.worldgen.feature.features.NoisePlantFeature;
import net.frozenblock.lib.worldgen.feature.features.UpwardsPillarFeature;
import net.frozenblock.lib.worldgen.feature.features.config.ColumnWithDiskFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.FadingDiskCarpetFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.FadingDiskFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.FadingDiskTagBiomeFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.FadingDiskTagFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.PathFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.PathSwapUnderWaterFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.PathSwapUnderWaterTagFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.PathTagFeatureConfig;
import net.frozenblock.lib.worldgen.feature.features.config.PillarFeatureConfig;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;

public class FrozenFeatures {

	public static final NoisePathFeature NOISE_PATH_FEATURE = new NoisePathFeature(PathFeatureConfig.CODEC);
	public static final NoisePathTagFeature NOISE_PATH_TAG_FEATURE = new NoisePathTagFeature(PathTagFeatureConfig.CODEC);
	public static final NoisePlantFeature NOISE_PLANT_FEATURE = new NoisePlantFeature(PathFeatureConfig.CODEC);
	public static final NoisePathSwapUnderWaterFeature NOISE_PATH_SWAP_UNDER_WATER_FEATURE = new NoisePathSwapUnderWaterFeature(PathSwapUnderWaterFeatureConfig.CODEC);
	public static final NoisePathSwapUnderWaterTagFeature NOISE_PATH_SWAP_UNDER_WATER_TAG_FEATURE = new NoisePathSwapUnderWaterTagFeature(PathSwapUnderWaterTagFeatureConfig.CODEC);
	public static final NoisePathUnderWaterFeature NOISE_PATH_UNDER_WATER_FEATURE = new NoisePathUnderWaterFeature(PathFeatureConfig.CODEC);
	public static final NoisePathTagUnderWaterFeature NOISE_PATH_TAG_UNDER_WATER_FEATURE = new NoisePathTagUnderWaterFeature(PathTagFeatureConfig.CODEC);
	public static final ColumnWithDiskFeature COLUMN_WITH_DISK_FEATURE = new ColumnWithDiskFeature(ColumnWithDiskFeatureConfig.CODEC);
	public static final UpwardsPillarFeature UPWARDS_PILLAR_FEATURE = new UpwardsPillarFeature(PillarFeatureConfig.CODEC);
	public static final DownwardsPillarFeature DOWNWARDS_PILLAR_FEATURE = new DownwardsPillarFeature(PillarFeatureConfig.CODEC);
	public static final CircularWaterloggedVegetationPatchFeature CIRCULAR_WATERLOGGED_VEGETATION_PATCH = new CircularWaterloggedVegetationPatchFeature(VegetationPatchConfiguration.CODEC);
	public static final FadingDiskTagFeature FADING_DISK_TAG_FEATURE = new FadingDiskTagFeature(FadingDiskTagFeatureConfig.CODEC);
	public static final FadingDiskTagExceptInBiomeFeature FADING_DISK_TAG_EXCEPT_IN_BIOME_FEATURE = new FadingDiskTagExceptInBiomeFeature(FadingDiskTagBiomeFeatureConfig.CODEC);
	public static final FadingDiskFeature FADING_DISK_FEATURE = new FadingDiskFeature(FadingDiskFeatureConfig.CODEC);
	public static final FadingDiskCarpetFeature FADING_DISK_CARPET_FEATURE = new FadingDiskCarpetFeature(FadingDiskCarpetFeatureConfig.CODEC);

	public static void init() {
		Registry.register(Registry.FEATURE, FrozenMain.id("noise_path_feature"), NOISE_PATH_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("noise_path_tag_feature"), NOISE_PATH_TAG_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("noise_plant_feature"), NOISE_PLANT_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("noise_path_swap_under_water_feature"), NOISE_PATH_SWAP_UNDER_WATER_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("noise_path_swap_under_water_tag_feature"), NOISE_PATH_SWAP_UNDER_WATER_TAG_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("noise_path_under_water_feature"), NOISE_PATH_UNDER_WATER_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("noise_path_tag_under_water_feature"), NOISE_PATH_TAG_UNDER_WATER_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("column_with_disk_feature"), COLUMN_WITH_DISK_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("upwards_pillar"), UPWARDS_PILLAR_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("downwards_pillar"), DOWNWARDS_PILLAR_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("circular_waterlogged_vegetation_patch"), CIRCULAR_WATERLOGGED_VEGETATION_PATCH);
		Registry.register(Registry.FEATURE, FrozenMain.id("fading_disk_tag_feature"), FADING_DISK_TAG_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("fading_disk_tag_except_in_biome_feature"), FADING_DISK_TAG_EXCEPT_IN_BIOME_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("fading_disk_feature"), FADING_DISK_FEATURE);
		Registry.register(Registry.FEATURE, FrozenMain.id("fading_disk_carpet_feature"), FADING_DISK_CARPET_FEATURE);
	}

}