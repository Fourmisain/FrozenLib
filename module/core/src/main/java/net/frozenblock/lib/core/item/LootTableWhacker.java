package net.frozenblock.lib.core.item;

import java.util.Arrays;
import java.util.List;
import net.frozenblock.lib.core.FrozenMain;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

public class LootTableWhacker extends Item {

    public LootTableWhacker(Properties settings) {
        super(settings);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        if (stack.hasCustomHoverName()) {
            if (stack.getHoverName().getString().contains(":")) {
                String id = stack.getHoverName().getString();
                List<String> strings = Arrays.stream(id.split(":")).toList();
                ResourceLocation location = new ResourceLocation(strings.get(0), strings.get(1));
                if (level.getBlockEntity(blockPos) instanceof RandomizableContainerBlockEntity loot) {
                    loot.lootTable = location;
                    FrozenMain.log(location.toString(), true);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

}
