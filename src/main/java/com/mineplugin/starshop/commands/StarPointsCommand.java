package com.mineplugin.starshop.commands;

import com.mineplugin.starshop.StarShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StarPointsCommand implements CommandExecutor {

    private final StarShop plugin;

    public StarPointsCommand(StarShop plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以使用此命令！");
            return true;
        }

        Player player = (Player) sender;

        // 处理管理员配置菜单指令
        if (args.length > 0 && args[0].equalsIgnoreCase("view")) {
            if (player.hasPermission("starshop.admin") || player.isOp()) {
                plugin.getConfigMenu().open(player);
            } else {
                player.sendMessage("§c你没有权限使用此命令！");
            }
            return true;
        }

        // 打开玩家商店菜单
        plugin.getShopMenu().open(player);
        return true;
    }
}
