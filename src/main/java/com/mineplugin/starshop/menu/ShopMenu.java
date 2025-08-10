package com.mineplugin.starshop.menu;

import com.mineplugin.starshop.StarShop;
import com.mineplugin.starshop.shop.ShopItem;
import me.lokka30.playerpoints.api.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopMenu {

    private final StarShop plugin;
    private final Inventory menu;

    public ShopMenu(StarShop plugin) {
        this.plugin = plugin;
        // 创建54格的箱子界面，标题为"星币商店"
        this.menu = Bukkit.createInventory(null, 54, "星币商店");
        updateMenu();
    }

    public void open(Player player) {
        updateMenu();
        player.openInventory(menu);
    }

    public void updateMenu() {
        menu.clear();
        
        // 填充商店物品
        for (ShopItem item : plugin.getConfigManager().getShopItems()) {
            if (item.getSlot() >= 0 && item.getSlot() < 54) {
                ItemStack displayItem = item.getItem().clone();
                ItemMeta meta = displayItem.getItemMeta();
                
                if (meta != null) {
                    List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
                    lore.add("");
                    lore.add("§6价格: " + item.getCost() + " 星币");
                    meta.setLore(lore);
                    displayItem.setItemMeta(meta);
                }
                
                menu.setItem(item.getSlot(), displayItem);
            }
        }
    }

    public void handleClick(Player player, int slot) {
        ShopItem item = plugin.getConfigManager().getShopItem(slot);
        
        if (item == null) {
            return;
        }
        
        PlayerPointsAPI api = plugin.getPlayerPoints().getAPI();
        int playerPoints = api.look(player.getUniqueId());
        
        // 检查点券是否足够
        if (playerPoints >= item.getCost()) {
            // 扣除点券
            api.take(player.getUniqueId(), item.getCost());
            
            // 执行命令
            for (String cmd : item.getCommands()) {
                // 替换命令中的变量（如玩家名）
                String processedCmd = cmd.replace("{player}", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), processedCmd);
            }
            
            player.sendMessage("§a购买成功！已扣除 " + item.getCost() + " 星币。");
        } else {
            player.sendMessage("§c星币不足！需要 " + item.getCost() + " 星币，但你只有 " + playerPoints + " 星币。");
        }
        
        // 重新打开菜单
        open(player);
    }
}
