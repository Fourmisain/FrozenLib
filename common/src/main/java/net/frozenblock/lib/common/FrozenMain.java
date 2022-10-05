package net.frozenblock.lib.common;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.frozenblock.lib.common.interfaces.EntityLoopingSoundInterface;
import net.frozenblock.lib.common.registry.FrozenRegistry;
import net.frozenblock.lib.common.replacements_and_lists.BlockScheduledTicks;
import net.frozenblock.lib.common.replacements_and_lists.BonemealBehaviors;
import net.frozenblock.lib.common.sound.FrozenSoundPackets;
import net.frozenblock.lib.common.sound.FrozenSoundPredicates;
import net.frozenblock.lib.common.sound.MovingLoopingSoundEntityManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLogger;

public class FrozenMain {
    public static final String MOD_ID = "frozenblocklib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final NOPLogger LOGGER4 = NOPLogger.NOP_LOGGER;

    public static void init() {
        FrozenSoundPredicates.init();

        if (Platform.isDevelopmentEnvironment()) {
            BlockScheduledTicks.ticks.put(Blocks.DIAMOND_BLOCK, (state, world, pos, random) -> world.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), 3));
            //StructurePoolElementIdReplacements.resourceLocationReplacements.put(new ResourceLocation("ancient_city/city_center/city_center_1"), id("ancient_city/city_center/city_center_2"));
            //StructurePoolElementIdReplacements.resourceLocationReplacements.put(new ResourceLocation("ancient_city/city_center/city_center_2"), id("ancient_city/city_center/city_center_2"));
            //StructurePoolElementIdReplacements.resourceLocationReplacements.put(new ResourceLocation("ancient_city/city_center/city_center_3"), id("ancient_city/city_center/city_center_2"));
            RegisterDev.init();
            BonemealBehaviors.bonemeals.put(Blocks.STONE, (context, level, pos, state, face, horizontal) -> {
                if (!level.isClientSide) {
                    level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
                    level.setBlockAndUpdate(pos, Blocks.DIAMOND_BLOCK.defaultBlockState());
                    return true;
                }
                return false;
            });
        }

        FrozenRegistry.init();

        receiveSoundSyncPacket();
    }


    //IDENTIFIERS
    public static final ResourceLocation FLYBY_SOUND_PACKET = id("flyby_sound_packet");
    public static final ResourceLocation MOVING_RESTRICTION_LOOPING_SOUND_PACKET = id("moving_restriction_looping_sound_packet");
    public static final ResourceLocation STARTING_RESTRICTION_LOOPING_SOUND_PACKET = id("starting_moving_restriction_looping_sound_packet");
    public static final ResourceLocation MOVING_RESTRICTION_SOUND_PACKET = id("moving_restriction_sound_packet");
    public static final ResourceLocation COOLDOWN_CHANGE_PACKET = id("cooldown_change_packet");
    public static final ResourceLocation REQUEST_LOOPING_SOUND_SYNC_PACKET = id("request_looping_sound_sync_packet");

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static String string(String path) {
        return id(path).toString();
    }

    public static void log(String string, boolean should) {
        if (should) {
            LOGGER.info(string);
        }
    }

    private static void receiveSoundSyncPacket() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), FrozenMain.REQUEST_LOOPING_SOUND_SYNC_PACKET, (byteBuf, context) -> {
            int id = byteBuf.readVarInt();
            Level dimension = context.getPlayer().getLevel();
            if (!(context.getPlayer() instanceof ServerPlayer serverPlayer)) {
                throw new IllegalStateException("ur player isnt a server player lol");
            } else {
                context.queue(() -> {
                    if (dimension != null) {
                        Entity entity = dimension.getEntity(id);
                        if (entity != null) {
                            if (entity instanceof LivingEntity living) {
                                for (MovingLoopingSoundEntityManager.SoundLoopNBT nbt : ((EntityLoopingSoundInterface) living).getSounds().getSounds()) {
                                    FrozenSoundPackets.createMovingRestrictionLoopingSound(serverPlayer, entity, Registry.SOUND_EVENT.get(nbt.getSoundEventID()), SoundSource.valueOf(SoundSource.class, nbt.getOrdinal()), nbt.volume, nbt.pitch, nbt.restrictionID);
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}