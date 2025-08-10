package com.mineplugin.starshop.config;

import com.mineplugin.starshop.StarShop;
import com.mineplugin.starshop.shop.ShopItem;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final StarShop plugin;
    private final File configFile;
    private YamlConfiguration config;
    private List<ShopItem> shopItems;

    public ConfigManager(StarShop plugin) {
        this.plugin = plugin;
        // 创建配置文件路径 (plugins/staershop/config/shop.yml)
        this.configFile = new File(plugin.getDataFolder() + "/config", "shop.yml");
        this.shopItems = new ArrayList<>();
        createConfigFile();
    }

    private void createConfigFile() {
        // 创建插件目录
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        // 创建配置目录
        File configDir = new File(plugin.getDataFolder() + "/config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        
        // 创建配置文件（如果不存在）
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("无法创建配置文件: " + e.getMessage());
            }
        }
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        shopItems.clear();
        
        // 从配置文件加载商店物品
        if (config.contains("items")) {
            for (String key : config.getConfigurationSection("items").getKeys(false)) {
                try {
                    int slot = Integer.parseInt(key);
                    ItemStack item = config.getItemStack("items." + key + ".item");
                    int cost = config.getInt("items." + key + ".cost");
                    List<String> commands = config.getStringList("items." + key + ".commands");
                    
                    shopItems.add(new ShopItem(slot, item, cost, commands));
                } catch (Exception e) {
                    plugin.getLogger().warning("加载商店物品时出错 (" + key + "): " + e.getMessage());
                }
            }
        }
    }

    public void saveConfig() {
        config = new YamlConfiguration();
        
        // 保存商店物品到配置文件
        for (ShopItem item : shopItems) {
            String path = "items." + item.getSlot();
            config.set(path + ".item", item.getItem());
            config.set(path + ".cost", item.getCost());
            config.set(path + ".commands", item.getCommands());
        }
        
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("保存配置文件时出错: " + e.getMessage());
        }
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }
    
    public void setShopItems(List<ShopItem> items) {
        this.shopItems = items;
    }
    
    public void addShopItem(ShopItem item) {
        // 先移除相同槽位的物品
        shopItems.removeIf(i -> i.getSlot() == item.getSlot());
        shopItems.add(item);
    }
    
    public void removeShopItem(int slot) {
        shopItems.removeIf(item -> item.getSlot() == slot);
    }
    
    public ShopItem getShopItem(int slot) {
        for (ShopItem item : shopItems) {
            if (item.getSlot() == slot) {
                return item;
            }
        }
        return null;
    }
    
    // 从物品的Lore中解析价格和命令
    public ShopItem parseShopItemFromItemStack(int slot, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return null;
        
        List<String> lore = meta.getLore();
        int cost = 0;
        List<String> commands = new ArrayList<>();
        
        // 解析Lore中的价格和命令
        for (String line : lore) {
            line = line.trim();
            
            if (line.startsWith("§6价格: ")) {
                try {
                    String costStr = line.substring(5).replace(" 星币", "").trim();
                    cost = Integer.parseInt(costStr);
                } catch (NumberFormatException e) {
                    // 格式错误，使用默认价格
                    cost = 0;
                }
            } else if (line.startsWith("§7命令: ")) {
                String cmd = line.substring(5).trim();
                commands.add(cmd);
            }
        }
        
        // 如果没有找到价格，默认设置为50
        if (cost == 0) {
            cost = 50;
        }
        
        // 如果没有命令，添加一个默认命令
        if (commands.isEmpty()) {
            commands.add("say {player} 购买了物品");
        }
        
        return new ShopItem(slot, item, cost, commands);
    }
}
