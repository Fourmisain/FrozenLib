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

package net.frozenblock.lib.events.api;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.frozenblock.lib.events.impl.EventType;
import net.minecraft.resources.ResourceLocation;

public class FrozenEvents {

	private static final List<Event<?>> REGISTERED_EVENTS = new ArrayList<>();

	public static <T> Event<T> createEnvironmentEvent(Class<? super T> type, Function<T[], T> invokerFactory) {
		var event = EventFactory.createArrayBacked(type, invokerFactory);

		register(event, type);

		return event;
	}

	public static <T> Event<T> createEnvironmentEvent(Class<T> type, T emptyInvoker, Function<T[], T> invokerFactory) {
		var event = EventFactory.createArrayBacked(type, emptyInvoker, invokerFactory);

		register(event, type);

		return event;
	}

	public static <T> void register(Event<T> event, Class<? super T> type) {
		if (!REGISTERED_EVENTS.contains(event)) {
			REGISTERED_EVENTS.add(event);
			for (var eventType : EventType.VALUES) {
				if (eventType.listener().isAssignableFrom(type)) {
					List<?> entrypoints = FabricLoader.getInstance().getEntrypoints(eventType.entrypoint(), eventType.listener());

					for (Object entrypoint : entrypoints) {
						var map = new Object2ObjectOpenHashMap<Class<?>, ResourceLocation>();

						if (type.isAssignableFrom(entrypoint.getClass())) {
							var phase = map.getOrDefault(type, Event.DEFAULT_PHASE);
							event.register(phase, (T) entrypoint);
						}
					}

					break;
				}
			}
		}
	}
}
