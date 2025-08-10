package com.mineplugin.starshop.menu;

import com.mineplugin.starshop.StarShop;
import com.mineplugin.starshop.shop.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ConfigMenu {

    private final StarShop plugin;
    private final Inventory menu;

    public ConfigMenu(StarShop plugin) {
        this.plugin = plugin;
        this.menu = Bukkit.createInventory(null, 54, "星币商店配置");
        updateMenu();
    }

    public void open(Player player) {
        updateMenu();
        player.openInventory(menu);
    }

    public void updateMenu() {
        menu.clear();
        
        // 添加一个示例说明物品
        ItemStack infoItem = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.setDisplayName("§a配置说明");
        List<String> infoLore = new ArrayList<>();
        infoLore.add("§7将物品放入格子来设置商品");
        infoLore.add("§7在物品Lore中设置:");
        infoLore.add("§7- §6价格: 50 §7(设置价格)");
        infoLore.add("§7- §7命令: give {player} diamond 1 §7(设置执行命令)");
        infoLore.add("§7关闭菜单自动保存配置");
        infoMeta.setLore(infoLore);
        infoItem.setItemMeta(infoMeta);
        menu.setItem(49, infoItem);
        
        // 填充当前配置的物品
        for (ShopItem item : plugin.getConfigManager().getShopItems()) {
            if (item.getSlot() >= 0 && item.getSlot() < 54 && item.getSlot() != 49) {
                menu.setItem(item.getSlot(), item.getItem());
            }
        }
    }

    public void handleClick(Player player, int slot, ItemStack clickedItem) {
        // 忽略说明物品的槽位
        if (slot == 49) return;
        
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            // 移除槽位中的物品配置
            plugin.getConfigManager().removeShopItem(slot);
            return;
        }
        
        // 从物品中解析商店物品信息
        ShopItem newItem = plugin.getConfigManager().parseShopItemFromItemStack(slot, clickedItem);
        if (newItem != null) {
            plugin.getConfigManager().addShopItem(newItem);
        }
    }

    public void saveMenu(Player player) {
        // 保存配置
        plugin.getConfigManager().saveConfig();
        // 更新商店菜单
        plugin.getShopMenu().updateMenu();
        // 提示玩家
        player.sendMessage("§a配置已保存！");
    }
}
