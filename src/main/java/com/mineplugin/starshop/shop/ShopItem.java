package com.mineplugin.starshop.shop;

import org.bukkit.inventory.ItemStack;
import java.util.List;

public class ShopItem {

    private final int slot;
    private final ItemStack item;
    private final int cost;
    private final List<String> commands;

    public ShopItem(int slot, ItemStack item, int cost, List<String> commands) {
        this.slot = slot;
        this.item = item;
        this.cost = cost;
        this.commands = commands;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }

    public List<String> getCommands() {
        return commands;
    }
}
