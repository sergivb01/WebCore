package net.veilmc.webcore.commands;

import net.veilmc.webcore.Main;
import net.veilmc.webcore.utils.BCrypt;
import net.veilmc.webcore.utils.HashUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.util.logging.PlatformLogger;

import java.util.Objects;


public class RegisterCommand implements CommandExecutor {
    private Main main;

    public RegisterCommand(Main plugin) {
        this.main = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "no.");
            return false;
        }

        Player player = (Player) sender;

        if(!main.getStorageBackend().checkProfile(player)){
            player.sendMessage(ChatColor.RED + "Your profile does not exist, please contact an staff member.");
            return false;
        }

        if(main.getStorageBackend().checkRegistered(player)){
           player.sendMessage(ChatColor.RED + "You are already registered!");
            return false;
        }

        if(args.length <= 1){
            player.sendMessage(ChatColor.RED + "/register password password");
            return false;
        }

        if(!Objects.equals(args[0], args[1])){
            player.sendMessage(ChatColor.RED + "Password do not match.");
            return false;
        }

        main.getStorageBackend().registerPlayer(player, main.getHashUtils().getHash(args[0]));
        player.sendMessage(ChatColor.RED + "Registered with password " + ChatColor.BOLD + args[0]);

        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Player registred: " + player.getName());

        return true;
    }
}