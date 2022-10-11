package net.frozenblock.lib.tags;

import net.fabricmc.loader.api.FabricLoader;
import net.frozenblock.lib.FrozenMain;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class FrozenBlockTags {
    public static final TagKey<Block> DRIPSTONE_CAN_DRIP_ON =
            of(FabricLoader.getInstance().isDevelopmentEnvironment() ?
                    "dripstone_can_drip_testing" : "dripstone_can_drip");

    private FrozenBlockTags() {
    }

    private static TagKey<Block> of(String path) {
        return TagKey.create(Registry.BLOCK_REGISTRY, FrozenMain.id(path));
    }
}