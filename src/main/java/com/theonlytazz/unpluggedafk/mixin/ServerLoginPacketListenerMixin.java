package com.theonlytazz.unpluggedafk.mixin;

import com.mojang.authlib.GameProfile;
import com.theonlytazz.unpluggedafk.player.OfflinePlayerManager;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginPacketListenerImpl.class)
abstract class ServerLoginPacketListenerMixin {
    @Shadow public GameProfile gameProfile;

    @Inject(
            method = "handleAcceptedLogin",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;canPlayerLogin(Ljava/net/SocketAddress;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/network/chat/Component;")
    )
    private void unpluggedAfk$replaceShadowBeforeLogin(CallbackInfo callback) {
        if (gameProfile != null) OfflinePlayerManager.get().prepareRealLogin(gameProfile.getId());
    }
}
