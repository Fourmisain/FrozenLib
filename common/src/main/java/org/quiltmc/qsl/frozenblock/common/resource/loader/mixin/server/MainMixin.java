/*
 * Copyright 2021-2022 QuiltMC
 * Modified to work on Architectury
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

package org.quiltmc.qsl.frozenblock.common.resource.loader.mixin.server;

import net.frozenblock.lib.common.events.FrozenLifecycleEvents;
import net.minecraft.server.Main;
import net.minecraft.server.WorldStem;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Modified to work on Architectury
 */
@Mixin(Main.class)
public class MainMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(
            method = "main",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/Util;blockUntilDone(Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;",
                    remap = true
            ),
            remap = false
    )
    private static void onStartReloadResources(String[] strings, CallbackInfo ci) {
        FrozenLifecycleEvents.START_DATA_PACK_RELOAD.invoker().onStartDataPackReload(null, null); // First reload
    }

    @ModifyVariable(method = "main", at = @At(value = "STORE"), remap = false)
    private static WorldStem onSuccessfulReloadResources(WorldStem resources) {
        FrozenLifecycleEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, resources.resourceManager(), true);
        return resources; // noop
    }

    @ModifyArg(
            method = "main",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V"
            ),
            index = 1,
            remap = false
    )
    private static Throwable onFailedReloadResources(Throwable exception) {
        FrozenLifecycleEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(null, null, exception == null);
        return exception; // noop
    }
}
