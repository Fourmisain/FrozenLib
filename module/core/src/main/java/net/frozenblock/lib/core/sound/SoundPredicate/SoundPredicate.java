package net.frozenblock.lib.core.sound.SoundPredicate;

import net.frozenblock.lib.core.FrozenMain;
import net.frozenblock.lib.core.registry.FrozenRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public final class SoundPredicate<T extends Entity> {

    public static void register(ResourceLocation id, LoopPredicate<?> predicate) {
		Registry.register(FrozenRegistry.SOUND_PREDICATE_SYNCED, id, new SoundPredicate<>(predicate));
    }

	public static void registerUnsynced(ResourceLocation id, LoopPredicate<?> predicate) {
		Registry.register(FrozenRegistry.SOUND_PREDICATE, id, new SoundPredicate<>(predicate));
	}

	private final LoopPredicate<T> predicate;

	public SoundPredicate(LoopPredicate<T> predicate) {
		this.predicate = predicate;
	}

    public static LoopPredicate<?> getPredicate(@Nullable ResourceLocation id) {
        if (id != null) {
            if (FrozenRegistry.SOUND_PREDICATE_SYNCED.containsKey(id)) {
				SoundPredicate<?> predicate = FrozenRegistry.SOUND_PREDICATE_SYNCED.get(id);
				if (predicate != null) {
					return predicate.predicate;
				}
			} else if (FrozenRegistry.SOUND_PREDICATE.containsKey(id)) {
				SoundPredicate<?> predicate = FrozenRegistry.SOUND_PREDICATE.get(id);
				if (predicate != null) {
					return predicate.predicate;
				}
			}
			FrozenMain.LOGGER.error("Unable to find sound predicate " + id + "! Using default sound predicate instead!");
        }
        return DEFAULT;
    }

    @FunctionalInterface
    public interface LoopPredicate<T extends Entity> {
        boolean test(Entity entity);
    }

    public static LoopPredicate<Entity> DEFAULT = entity -> !entity.isSilent();
    public static ResourceLocation DEFAULT_ID = FrozenMain.id("default");
    public static LoopPredicate<Entity> NOT_SILENT_AND_ALIVE = entity -> !entity.isSilent();
    public static ResourceLocation NOT_SILENT_AND_ALIVE_ID = FrozenMain.id("not_silent_and_alive");

    public static void init() {
        register(FrozenMain.id("default"), DEFAULT);
        register(FrozenMain.id("not_silent_and_alive"), NOT_SILENT_AND_ALIVE);
    }
}
