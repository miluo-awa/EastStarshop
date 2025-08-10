package com.mineplugin.starshop.listeners;

import com.mineplugin.starshop.StarShop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

    private final StarShop plugin;

    public MenuListener(StarShop plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();

        // 处理商店菜单点击
        if (title.equals("星币商店")) {
            event.setCancelled(true);
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(event.getView().getTopInventory())) {
                plugin.getShopMenu().handleClick(player, event.getSlot());
            }
        }
        // 处理配置菜单点击
        else if (title.equals("星币商店配置")) {
            event.setCancelled(true);
            if (event.getClickedInventory() != null && event.getClickedInventory().equals(event.getView().getTopInventory())) {
                plugin.getConfigMenu().handleClick(player, event.getSlot(), event.getCurrentItem());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getPlayer();
        String title = event.getView().getTitle();

        // 当关闭配置菜单时保存配置
        if (title.equals("星币商店配置")) {
            plugin.getConfigMenu().saveMenu(player);
        }
    }
}
