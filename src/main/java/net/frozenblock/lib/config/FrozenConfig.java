package net.frozenblock.lib.config;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
public final class FrozenConfig {

    public static ConfigCategory createSubCategory(ConfigEntryBuilder entryBuilder, ConfigCategory parentCategory, Component key, boolean expanded, Component tooltip, AbstractConfigListEntry... entries) {
        Preconditions.checkArgument(entryBuilder != null, "ConfigEntryBuilder is null");
        Preconditions.checkArgument(parentCategory != null, "Parent Category is null");
        Preconditions.checkArgument(key != null, "Sub Category key is null");
        Arrays.stream(entries).forEach(entry -> Preconditions.checkArgument(entry != null, "Config List Entry is null"));

        var subCategory = entryBuilder.startSubCategory(key, Arrays.stream(entries).toList());

        subCategory.setExpanded(expanded);
        if (tooltip != null) {
            subCategory.setTooltip(tooltip);
        }

        return parentCategory.addEntry(entryBuilder.startSubCategory(key, Arrays.stream(entries).toList())
                .setExpanded(expanded)
                .setTooltip(tooltip)
                .build()
        );
    }
}
