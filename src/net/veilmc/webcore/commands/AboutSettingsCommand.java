package net.veilmc.webcore.commands;

import net.veilmc.webcore.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Map;

public class AboutSettingsCommand implements CommandExecutor {
    private Main main;

    public AboutSettingsCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You may not execute that command.");
            return false;
        }

        Player p = (Player) sender;

        for (Map.Entry<String,Boolean> entry : main.getStorageBackend().getData(p).entrySet()) {
            p.sendMessage(ChatColor.AQUA + entry.getKey() + ": " + ChatColor.LIGHT_PURPLE + entry.getValue().toString());
        }


        return true;
    }
}