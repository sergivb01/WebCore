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
            sender.sendMessage(Main.PREFIX + ChatColor.RED + "You may not execute that command.");
            return false;
        }

        Player p = (Player) sender;

        p.sendMessage(Main.PREFIX + ChatColor.RED + "Todo...");


        return true;
    }
}