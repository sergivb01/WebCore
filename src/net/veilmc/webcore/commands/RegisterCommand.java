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
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return false;
        }

        Player player = (Player) sender;

        if(!main.getStorageBackend().checkProfile(player)){
            player.sendMessage(Main.PREFIX + ChatColor.RED + "Your profile does not exist, please contact a staff member.");
            return false;
        }

        if(!main.getStorageBackend().checkRegistered(player)){
           player.sendMessage(Main.PREFIX + ChatColor.RED + "You are already registered, you can login at " + ChatColor.YELLOW + "veilhcf.us");
            return false;
        }

        if(args.length <= 1){
            player.sendMessage(Main.PREFIX + ChatColor.RED + "/register <password> <confirmPassword>");
            return false;
        }

        if(!Objects.equals(args[0], args[1])){
            player.sendMessage(Main.PREFIX + ChatColor.RED + "The passwords you entered do not match.");
            return false;
        }

        main.getStorageBackend().registerPlayer(player, main.getHashUtils().getHash(args[0]));
        player.sendMessage(Main.PREFIX + ChatColor.RED + "You have registered with the password: " + ChatColor.BOLD + args[0]);

        Bukkit.getConsoleSender().sendMessage(Main.PREFIX + ChatColor.LIGHT_PURPLE + "Player registred: " + player.getName());

        return true;
    }
}