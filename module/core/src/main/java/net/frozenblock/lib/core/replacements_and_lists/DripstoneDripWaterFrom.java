package net.frozenblock.lib.core.replacements_and_lists;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;

public class DripstoneDripWaterFrom {

    public static Map<Block, InjectedOnDrip> map = new HashMap<>();

    @FunctionalInterface
    public interface InjectedOnDrip {
        void drip(ServerLevel world, PointedDripstoneBlock.FluidInfo fluidInfo, BlockPos pos);
    }

}
