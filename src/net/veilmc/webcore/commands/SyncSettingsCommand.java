package net.veilmc.webcore.commands;

import net.veilmc.webcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SyncSettingsCommand implements CommandExecutor {
    private Main main;

    public SyncSettingsCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(Bukkit.getOnlinePlayers().size() == 0){
            sender.sendMessage(ChatColor.RED + "There are no players.");
            return false;
        }

        for (Player player: Bukkit.getOnlinePlayers()) {
            main.getStorageBackend().saveSettings(player);
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Settings of " + player.getName() + " have been saved.");
        }

        sender.sendMessage(ChatColor.AQUA + "Settings have been synced.");

        return true;
    }
}