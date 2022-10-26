package net.frozenblock.lib.core.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FrozenProjectileDamageSource extends IndirectEntityDamageSource {
    public FrozenProjectileDamageSource(String name, Entity projectile, @Nullable Entity attacker) {
        super(name, projectile, attacker);
    }

    public static DamageSource source(String name, Entity projectile, @Nullable Entity attacker) {
        return new FrozenProjectileDamageSource(name, projectile, attacker);
    }

    public Component getLocalizedDeathMessage(LivingEntity entity) {
        Component text = this.getEntity() == null ? this.entity.getDisplayName() : this.getEntity().getDisplayName();
        ItemStack itemStack = this.getEntity() instanceof LivingEntity ? ((LivingEntity) this.getEntity()).getMainHandItem() : ItemStack.EMPTY;
        String string = "death.attack." + this.msgId;
        String string2 = string + ".item";
        return !itemStack.isEmpty() && itemStack.hasCustomHoverName() ? Component.translatable(string2, entity.getDisplayName(), text, itemStack.getDisplayName()) : Component.translatable(string, entity.getDisplayName(), text);
    }
}
