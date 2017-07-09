package net.veilmc.webcore.listeners;

import net.veilmc.webcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener{
    private Main main;

    public PlayerEvents(Main plugin) {
        this.main = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        if(!main.getStorageBackend().checkProfile(p)){
            main.getStorageBackend().createProfile(p);
            p.sendMessage(ChatColor.GOLD + "Your profile has been created!");
        }else{
            p.sendMessage(ChatColor.RED + "You profile already exists, not creating a new one...");
        }


    }



}
