package com.ricardthegreat.holdmetight.inventory;

import com.mojang.datafixers.util.Pair;
import com.ricardthegreat.holdmetight.init.MenuInit;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.common.CuriosRegistry;

public class HeldEntityInventoryMenu extends AbstractContainerMenu{

    static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{
        InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, 
        InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, 
        InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, 
        InventoryMenu.EMPTY_ARMOR_SLOT_HELMET};

    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    private final DataSlot ownerId = DataSlot.standalone();

    private final Player owner;

    public HeldEntityInventoryMenu(int containerId, Inventory playerInv){
        this(containerId, new Inventory(playerInv.player), playerInv);
    }

    public HeldEntityInventoryMenu(int containerId, Inventory targetInv, Inventory playerInv){
        super(MenuInit.HELD_PLAYER_MENU.get(), containerId);
        this.owner = targetInv.player;

        createTargetInvSlots(targetInv);
        createPlayerInventorySlots(playerInv);

        addDataSlot(ownerId);
        this.ownerId.set(owner.getId());
    }

    private void createTargetInvSlots(Inventory playerInv){
        for(int i = 0; i < 4; ++i) {
            final EquipmentSlot equipmentslot = SLOT_IDS[i];
            this.addSlot(new Slot(playerInv, 39 - i, 8, 12 + i * 18) {
                public void setByPlayer(ItemStack stack) {
                    HeldEntityInventoryMenu.onEquipItem(owner, equipmentslot, stack, this.getItem());
                    super.setByPlayer(stack);
                }

                public int getMaxStackSize() {
                    return 1;
                }

                public boolean mayPlace(ItemStack stack) {
                    return stack.canEquip(equipmentslot, owner);
                }

                public boolean mayPickup(Player stack) {
                    ItemStack itemstack = this.getItem();
                    return !itemstack.isEmpty() && !stack.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack) ? false : super.mayPickup(stack);
                }

                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return Pair.of(InventoryMenu.BLOCK_ATLAS, HeldEntityInventoryMenu.TEXTURE_EMPTY_SLOTS[equipmentslot.getIndex()]);
                }
            });
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + (i + 1) * 9, 8 + j * 18, 88 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 146));
        }

        this.addSlot(new Slot(playerInv, 40, 77, 66) {
            public void setByPlayer(ItemStack stack) {
                HeldEntityInventoryMenu.onEquipItem(owner, EquipmentSlot.OFFHAND, stack, this.getItem());
                super.setByPlayer(stack);
            }

            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }

    private void createPlayerInventorySlots(Inventory playerInv){
        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(playerInv, k1 + i1 * 9 + 9, 8 + k1 * 18, 174 + i1 * 18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(playerInv, j1, 8 + j1 * 18, 232));
        }
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int i, int j, boolean bool) {
        //TODO figure out what i, j and bool represent
        if (stack.getItem() instanceof EntityStandinItem) {
            return false;
        }
        return super.moveItemStackTo(stack, i, j, bool);
    }

    private static void onEquipItem(Player player, EquipmentSlot slot, ItemStack stack, ItemStack stack2) {
        Equipable equipable = Equipable.get(stack);
        if (equipable != null) {
            player.onEquipItem(slot, stack2, stack);
        }
    }

    @Override
    public void clicked(int slot, int mouse, ClickType click, Player player) {
        if (slot < 0 || slot >= slots.size()) {
            super.clicked(slot, mouse, click, player);
        }else{
            ItemStack stack = slots.get(slot).getItem();

            if (!(stack.getItem() instanceof EntityStandinItem)) {
                super.clicked(slot, mouse, click, player);
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
        //TODO understand this, currently just copied from forge docs while i figure out other stuff


        // The quick moved slot stack
        ItemStack quickMovedStack = ItemStack.EMPTY;
        // The quick moved slot
        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);

        // If the slot is in the valid range and the slot is not empty
        if (quickMovedSlot != null && quickMovedSlot.hasItem()) {
            // Get the raw stack to move
            ItemStack rawStack = quickMovedSlot.getItem(); 
            // Set the slot stack to a copy of the raw stack
            quickMovedStack = rawStack.copy();

            /*
            The following quick move logic can be simplified to if in data inventory,
            try to move to player inventory/hotbar and vice versa for containers
            that cannot transform data (e.g. chests).
            */

            if (quickMovedSlotIndex == 0) { // If the quick move was performed on the data inventory result slot 
                if (!this.moveItemStackTo(rawStack, 5, 41, true)) { // Try to move the result slot into the player inventory/hotbar
                    // If cannot move, no longer quick move
                    return ItemStack.EMPTY;
                }
                
                // Perform logic on result slot quick move
                this.getSlot(quickMovedSlotIndex).onQuickCraft(rawStack, quickMovedStack);
            } else if (quickMovedSlotIndex >= 5 && quickMovedSlotIndex < 41) { // Else if the quick move was performed on the player inventory or hotbar slot
                if (!this.moveItemStackTo(rawStack, 1, 5, false)) { // Try to move the inventory/hotbar slot into the data inventory input slots
                    if (quickMovedSlotIndex < 32) { // If cannot move and in player inventory slot, try to move to hotbar
                        if (!this.moveItemStackTo(rawStack, 32, 41, false)) {
                            // If cannot move, no longer quick move
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(rawStack, 5, 32, false)) { // Else try to move hotbar into player inventory slot
                        // If cannot move, no longer quick move
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(rawStack, 5, 41, false)) { // Else if the quick move was performed on the data inventory input slots, try to move to player inventory/hotbar
                // If cannot move, no longer quick move
                return ItemStack.EMPTY;
            }

            if (rawStack.isEmpty()) {
                // If the raw stack has completely moved out of the slot, set the slot to the empty stack
                quickMovedSlot.set(ItemStack.EMPTY);
            } else {
                // Otherwise, notify the slot that that the stack count has changed
                quickMovedSlot.setChanged();
            }

            /*
            The following if statement and Slot#onTake call can be removed if the
            menu does not represent a container that can transform stacks (e.g.
            chests).
            */
            if (rawStack.getCount() == quickMovedStack.getCount()) {
                // If the raw stack was not able to be moved to another slot, no longer quick move
                return ItemStack.EMPTY;
            }
            // Execute logic on what to do post move with the remaining stack
            quickMovedSlot.onTake(player, rawStack);
        }

        return quickMovedStack; // Return the slot stack
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public DataSlot getOwnerId(){
        return this.ownerId;
    }
}
