package net.frozenblock.lib.common.interfaces;

import net.minecraft.world.item.Item;

public interface CooldownInterface {

    void changeCooldown(Item item, int additional);

    void onCooldownChanged(Item item, int additional);

}