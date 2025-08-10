package com.mineplugin.starshop;

import com.mineplugin.starshop.commands.StarPointsCommand;
import com.mineplugin.starshop.config.ConfigManager;
import com.mineplugin.starshop.listeners.MenuListener;
import com.mineplugin.starshop.menu.ConfigMenu;
import com.mineplugin.starshop.menu.ShopMenu;
import me.lokka30.playerpoints.PlayerPoints;
import org.bukkit.plugin.java.JavaPlugin;

public class StarShop extends JavaPlugin {

    private static StarShop instance;
    private PlayerPoints playerPoints;
    private ConfigManager configManager;
    private ShopMenu shopMenu;
    private ConfigMenu configMenu;

    @Override
    public void onEnable() {
        instance = this;
        
        // 检查PlayerPoints插件是否已安装
        if (!setupPlayerPoints()) {
            getLogger().severe("PlayerPoints插件未找到！请安装PlayerPoints 3.3.2版本。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        // 初始化配置管理器
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // 初始化菜单
        shopMenu = new ShopMenu(this);
        configMenu = new ConfigMenu(this);
        
        // 注册命令和监听器
        getCommand("starpoints").setExecutor(new StarPointsCommand(this));
        new MenuListener(this);
        
        getLogger().info("StarShop插件已成功启用！");
    }

    @Override
    public void onDisable() {
        getLogger().info("StarShop插件已禁用！");
        instance = null;
    }
    
    private boolean setupPlayerPoints() {
        if (getServer().getPluginManager().getPlugin("PlayerPoints") instanceof PlayerPoints) {
            playerPoints = (PlayerPoints) getServer().getPluginManager().getPlugin("PlayerPoints");
            return playerPoints != null;
        }
        return false;
    }
    
    public static StarShop getInstance() {
        return instance;
    }
    
    public PlayerPoints getPlayerPoints() {
        return playerPoints;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public ShopMenu getShopMenu() {
        return shopMenu;
    }
    
    public ConfigMenu getConfigMenu() {
        return configMenu;
    }
}
