package com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.preferences;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.capabilities.carry.CarryPosition;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CPlayerPreferencesSyncPacket {

    private final float maxScale;
    private final float minScale;
    private final float defaultScale;
    private final boolean othersCanChangeYourSize;
    private final boolean youCanChangeYourSize;

    private final boolean inventoryCanBeAccessed;
    private final boolean trapCarriedPlayer;
    private final boolean canBeTrappedWhileCarried;
    private final boolean canBePickedup;
    private final boolean canPickupOthers;

    private final UUID uuid;

    public CPlayerPreferencesSyncPacket(
        float maxScale, float minScale, float defaultScale, boolean othersCanChangeYourSize, boolean youCanChangeYourSize, 
        boolean inventoryCanBeAccessed, boolean trapCarriedPlayer, boolean canBeTrappedWhileCarried, boolean canBePickedup, boolean canPickupOthers, 
        UUID uuid){
        this.maxScale = maxScale;
        this.minScale = minScale;
        this.defaultScale = defaultScale;
        this.othersCanChangeYourSize = othersCanChangeYourSize;
        this.youCanChangeYourSize = youCanChangeYourSize;

        this.inventoryCanBeAccessed = inventoryCanBeAccessed;
        this.trapCarriedPlayer = trapCarriedPlayer;
        this.canBeTrappedWhileCarried = canBeTrappedWhileCarried;
        this.canBePickedup = canBePickedup;
        this.canPickupOthers = canPickupOthers;

        this.uuid = uuid;
    }

    public CPlayerPreferencesSyncPacket(FriendlyByteBuf buffer){
        this.maxScale = buffer.readFloat();
        this.minScale = buffer.readFloat();
        this.defaultScale = buffer.readFloat();
        this.othersCanChangeYourSize = buffer.readBoolean();
        this.youCanChangeYourSize = buffer.readBoolean();

        this.inventoryCanBeAccessed = buffer.readBoolean();
        this.trapCarriedPlayer = buffer.readBoolean();
        this.canBeTrappedWhileCarried = buffer.readBoolean();
        this.canBePickedup = buffer.readBoolean();
        this.canPickupOthers = buffer.readBoolean();

        this.uuid = buffer.readUUID();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeFloat(maxScale);
        buffer.writeFloat(minScale);
        buffer.writeFloat(defaultScale);
        buffer.writeBoolean(othersCanChangeYourSize);
        buffer.writeBoolean(youCanChangeYourSize);

        buffer.writeBoolean(inventoryCanBeAccessed);
        buffer.writeBoolean(trapCarriedPlayer);
        buffer.writeBoolean(canBeTrappedWhileCarried);
        buffer.writeBoolean(canBePickedup);
        buffer.writeBoolean(canPickupOthers);

        buffer.writeUUID(uuid);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePreferencesPacket(this, context))
        );
        
        context.get().setPacketHandled(true);
    }

    public void playerSyncablesUpdate(PlayerPreferences preferences){
        preferences.updateAllSyncables(
                maxScale, minScale, defaultScale, othersCanChangeYourSize, youCanChangeYourSize, 
                inventoryCanBeAccessed, trapCarriedPlayer, canBeTrappedWhileCarried, canBePickedup, canPickupOthers);
    }

    public UUID getUuid() {
        return uuid;
    }
}
