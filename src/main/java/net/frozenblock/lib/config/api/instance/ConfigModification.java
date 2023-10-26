/*
 * Copyright 2023 FrozenBlock
 * This file is part of FrozenLib.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.config.api.instance;

import net.frozenblock.lib.config.api.registry.ConfigRegistry;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

/**
 * Wrapper class for modifying configs
 * @param modification The consumer for applying modifications
 * @param <T> The type of the config class
 */
public record ConfigModification<T>(Consumer<T> modification) {
    public static <T> T modifyConfig(Config<T> config, T original) {
        try {
			// clone
			T instance = config.configClass().getConstructor().newInstance();
			copyInto(original, instance);

			// modify
			for (ConfigModification<T> modification : ConfigRegistry.getModificationsForConfig(config)) {
				modification.modification.accept(instance);
			}
			return instance;
		} catch (Exception ignored) {
			ignored.printStackTrace();
			return original;
		}
    }

    private static <T> void copyInto(T source, T destination) {
        Class<?> clazz = source.getClass();
        while (!clazz.equals(Object.class)) {
            for (Field field : clazz.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) continue;
                field.setAccessible(true);
                try {
                    field.set(destination, field.get(source));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}