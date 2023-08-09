/*
 * Copyright 2023 The Quilt Project
 * Copyright 2023 FrozenBlock
 * Modified to work on Fabric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.frozenblock.core.registry.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.frozenblock.lib.entrypoint.api.CommonEventEntrypoint;
import net.frozenblock.lib.event.api.FrozenEvents;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import org.jetbrains.annotations.NotNull;

/**
 * Events for listening to the manipulation of Minecraft's content registries.
 * <p>
 * The events are to be used for very low-level purposes, and callbacks are only called on registry manipulations
 * occurring after the event registration. This means that mod load order can affect what is picked up by these events.
 * <p>
 * For more high-level monitoring of registries, including methods to ease the inconvenience of mod load order,
 * use RegistryMonitor.
 * <p>
 * Modified to work on Fabric
 */
public final class RegistryEvents {
	private RegistryEvents() {
		throw new UnsupportedOperationException("RegistryEvents contains only static declarations.");
	}

	/**
	 * Gets the entry added event for a specific Minecraft registry.
	 * <p>
	 * The event is invoked upon the addition or assignment of an entry in the specified registry.
	 *
	 * @param registry the {@link Registry} for this event to listen for. Must be an instance of {@link MappedRegistry}.
	 * @param <V>      the entry type of the {@link Registry} to listen for
	 * @return the entry added event for the specified registry, which can have callbacks registered to it
	 * @throws ClassCastException if the registry is not a {@link MappedRegistry}
	 */
	public static <V> Event<EntryAdded<V>> getEntryAddEvent(Registry<V> registry) {
		return RegistryEventStorage.as((MappedRegistry<V>) registry).frozenLib_quilt$getEntryAddedEvent();
	}

	/**
	 * This event gets triggered when a new {@link RegistryAccess} gets created,
	 * but before it gets filled.
	 * <p>
	 * This event can be used to register callbacks to dynamic registries, or to pre-fill some values.
	 * <p>
	 * <strong>Important Note</strong>: The passed dynamic registry manager might not
	 * contain the registry, as this event is invoked for each layer of
	 * the combined registry manager, and each layer holds different registries.
	 * Use {@link RegistryAccess#registry} to prevent crashes.
	 */
	public static final Event<DynamicRegistrySetupCallback> DYNAMIC_REGISTRY_SETUP = FrozenEvents.createEnvironmentEvent(DynamicRegistrySetupCallback.class,
			callbacks -> context -> {
				for (var callback : callbacks) {
					callback.onDynamicRegistrySetup(context);
				}
			}
	);

	/**
	 * This event gets triggered when a new {@link RegistryAccess} gets created,
	 * after it has been filled with the registry entries specified by data packs.
	 * <p>
	 * This event can be used to register callbacks to dynamic registries, or to inspect values.
	 * <p>
	 * <strong>Important Note</strong>: The passed dynamic registry manager might not
	 * contain the registry, as this event is invoked for each layer of
	 * the combined registry manager, and each layer holds different registries.
	 * Use {@link RegistryAccess#registry} to prevent crashes.
	 */
	public static final Event<DynamicRegistryLoadedCallback> DYNAMIC_REGISTRY_LOADED = FrozenEvents.createEnvironmentEvent(DynamicRegistryLoadedCallback.class,
			callbacks -> registryManager -> {
				for (var callback : callbacks) {
					callback.onDynamicRegistryLoaded(registryManager);
				}
			}
	);

	/**
	 * Functional interface to be implemented on callbacks for {@link #getEntryAddEvent(Registry)}.
	 *
	 * @param <V> the entry type of the {@link Registry} being listened for
	 * @see #getEntryAddEvent(Registry)
	 */
	@FunctionalInterface
	public interface EntryAdded<V> {
		/**
		 * Called when an entry in this callback's event's {@link Registry} has an entry added or assigned.
		 *
		 * @param context an object containing information regarding the registry, entry object, and identifier of the entry
		 *                being registered
		 */
		void onAdded(RegistryEntryContext<V> context);
	}

	@FunctionalInterface
	public interface DynamicRegistrySetupCallback extends CommonEventEntrypoint {
		/**
		 * Called when a new {@link RegistryAccess} gets created,
		 * but before it gets filled.
		 * <p>
		 * <strong>Important Note</strong>: The passed dynamic registry manager might not
		 * contain the registry, as this event is invoked for each layer of
		 * the combined registry manager, and each layer holds different registries.
		 * Use {@link RegistryAccess#registry} to prevent crashes.
		 *
		 * @param context the dynamic registry manager setup context
		 */
		void onDynamicRegistrySetup(@NotNull DynamicRegistryManagerSetupContext context);
	}

	@FunctionalInterface
	public interface DynamicRegistryLoadedCallback extends CommonEventEntrypoint {
		/**
		 * Called when a new {@link RegistryAccess} gets created,
		 * after it has been filled with the registry entries specified by data packs.
		 * <p>
		 * <strong>Important Note</strong>: The passed dynamic registry manager might not
		 * contain the registry, as this event is invoked for each layer of
		 * the combined registry manager, and each layer holds different registries.
		 * Use {@link RegistryAccess#registry} to prevent crashes.
		 *
		 * @param registryManager the registry manager
		 */
		void onDynamicRegistryLoaded(@NotNull RegistryAccess registryManager);
	}
}
