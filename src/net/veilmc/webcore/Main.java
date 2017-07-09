package net.veilmc.webcore;

import lombok.Getter;
import net.veilmc.webcore.backend.DatabaseCredentials;
import net.veilmc.webcore.backend.StorageBackend;
import net.veilmc.webcore.backend.type.StorageBackendMySQL;
import net.veilmc.webcore.commands.RegisterCommand;
import net.veilmc.webcore.utils.BCrypt;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.veilmc.webcore.commands.InfoCommand;
import net.veilmc.webcore.listeners.PlayerEvents;

public class Main extends JavaPlugin {
    @Getter private static String salt = BCrypt.gensalt();
    @Getter private static Main instance;
    @Getter private StorageBackend storageBackend;
    private PluginManager manager = Bukkit.getServer().getPluginManager();

    @Override
    public void onEnable() {
        instance = this;


        getCommand("info").setExecutor(new InfoCommand(this));
        getCommand("register").setExecutor(new RegisterCommand(this));

        //storageBackend = new StorageBackendMySQL(new DatabaseCredentials("158.69.123.169", 3306, "remote", "hyuK3tF5bMHfaxY8njVs7Mj19", "webcore"));
        storageBackend = new StorageBackendMySQL(new DatabaseCredentials("localhost", 3306, "root", "sergivb01", "webcore"));

        manager.registerEvents(new PlayerEvents(this), this);
    }

    @Override
    public void onDisable() {

        storageBackend.closeConnections();




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