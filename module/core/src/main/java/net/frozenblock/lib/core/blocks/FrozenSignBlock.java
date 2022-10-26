package net.frozenblock.lib.core.blocks;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;

public class FrozenSignBlock extends StandingSignBlock {
    public final ResourceLocation lootTable;

    public FrozenSignBlock(Properties settings, WoodType signType, ResourceLocation lootTable) {
        super(settings, signType);
        this.lootTable = lootTable;
    }

    @Override
    public ResourceLocation getLootTable() {
        if (!Objects.equals(this.drops, this.lootTable)) {
            this.drops = this.lootTable;
        }

        return this.drops;
    }
}
