package net.frozenblock.lib.testmod.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.frozenblock.lib.config.FrozenConfig;
import net.minecraft.resources.ResourceLocation;

import static net.frozenblock.lib.testmod.config.TestConfig.text;
import static net.frozenblock.lib.testmod.config.TestConfig.tooltip;

@Config(name = "general")
public class GeneralTestConfig implements ConfigData {

	public boolean testBoolean = true;

	@ConfigEntry.Gui.CollapsibleObject
	public SubMenu subMenu = new SubMenu();

	public static class SubMenu {
		public boolean testSubMenuBoolean = true;
	}

	@Environment(EnvType.CLIENT)
	public static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
		var config = TestConfig.get().general;
		var subMenu = config.subMenu;
		category.setBackground(new ResourceLocation("textures/block/packed_mud.png"));
		var test = category.addEntry(entryBuilder.startBooleanToggle(text("test_boolean"), config.testBoolean)
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> config.testBoolean = newValue)
				.setTooltip(tooltip("test_boolean"))
				.build()
		);

		var testSubMenuBoolean = entryBuilder.startBooleanToggle(text("test_submenu_boolean"), subMenu.testSubMenuBoolean)
				.setDefaultValue(true)
				.setSaveConsumer(newValue -> subMenu.testSubMenuBoolean = newValue)
				.setTooltip(tooltip("test_submenu_boolean"))
				.build();

		var testSubMenuCategory = FrozenConfig.createSubCategory(entryBuilder, category, text("test_subcategory"),
				false,
				tooltip("test_subcategory"),
				testSubMenuBoolean
		);
	}
}