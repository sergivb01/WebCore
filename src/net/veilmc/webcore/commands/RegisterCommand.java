package net.veilmc.webcore.commands;

import net.veilmc.webcore.Main;
import net.veilmc.webcore.utils.BCrypt;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        //TODO: Check if is registred....

        Player player = (Player) sender;

        if(args.length <= 1){
            player.sendMessage(ChatColor.RED + "/register password password");
            return false;
        }

        if(!Objects.equals(args[0], args[1])){
            player.sendMessage(ChatColor.RED + "Password do not match.");
            return false;
        }

        main.getStorageBackend().createProfile(player, BCrypt.hashpw(args[0], Main.getSalt()));
        player.sendMessage(ChatColor.RED + "Registred with " + args[0]);


        return true;
    }
}