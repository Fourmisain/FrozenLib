/*
 * Copyright 2022 FrozenBlock
 * This file is part of FrozenLib.
 *
 * FrozenLib is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * FrozenLib is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with FrozenLib. If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.mixin.server;

import com.google.common.collect.Maps;
import java.util.Map;
import net.frozenblock.lib.item.impl.CooldownInterface;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemCooldowns.class)
public class ItemCooldownsMixin implements CooldownInterface {

    @Final
    @Shadow
    @Mutable
    public final Map<Item, ItemCooldowns.CooldownInstance> cooldowns = Maps.newHashMap();

	@Unique
	@Override
    public void changeCooldown(Item item, int additional) {
        if (this.cooldowns.containsKey(item)) {
            ItemCooldowns.CooldownInstance cooldown = this.cooldowns.get(item);
            this.cooldowns.put(item, new ItemCooldowns.CooldownInstance(cooldown.startTime, cooldown.endTime + additional));
            this.onCooldownChanged(item, additional);
        }
    }

	@Unique
	@Override
    public void onCooldownChanged(Item item, int additional) {
    }

}