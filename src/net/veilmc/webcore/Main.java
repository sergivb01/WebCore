package net.veilmc.webcore;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.veilmc.webcore.commands.InfoCommand;
import net.veilmc.webcore.listeners.PlayerEvents;

public class Main extends JavaPlugin {
    @Getter private static String salt = "todo";
    private static Main instance;
    private PluginManager manager = Bukkit.getServer().getPluginManager();

    @Override
    public void onEnable() {
        instance = this;

        getCommand("info").setExecutor(new InfoCommand(this));

        manager.registerEvents(new PlayerEvents(this), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }


    /*
    public static String getRanks(Player player) {
        String message = ""; PermissionUser permissionUser = PermissionsEx.getUser(player);
        for (String ranks : permissionUser.getGroupNames()) {message += ranks + ", ";}
        if (message.length() > 2) {message = message.substring(0, message.length() - 2);}
        if (message.length() == 0) {message = "User";}
        if (message.equalsIgnoreCase("default")) {message = "User";}
        return message;
    }*/


}