package net.frozenblock.lib.core.testmod.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;

final class ModMenuConfigInteractionHandler {

    static ConfigScreenFactory<Screen> buildScreen() {
        return TestConfig::buildScreen;
    }
}
