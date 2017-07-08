package net.veilmc.webcore.commands;

import net.veilmc.webcore.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class InfoCommand implements CommandExecutor {
    private Main main;

    public InfoCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Hey");
        return true;
    }
}