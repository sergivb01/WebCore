package net.veilmc.webcore.listeners;

import net.veilmc.webcore.Main;
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

        p.sendMessage(ChatColor.RED + "https://veilhcf.us/u/" + p.getName());
    }



}
