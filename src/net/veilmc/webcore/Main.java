package net.veilmc.webcore;

import lombok.Getter;
import net.veilmc.webcore.backend.DatabaseCredentials;
import net.veilmc.webcore.backend.StorageBackend;
import net.veilmc.webcore.backend.type.StorageBackendMySQL;
import net.veilmc.webcore.commands.AboutSettingsCommand;
import net.veilmc.webcore.commands.RegisterCommand;
import net.veilmc.webcore.utils.HashUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.veilmc.webcore.commands.SyncSettingsCommand;
import net.veilmc.webcore.listeners.PlayerEvents;

public class Main extends JavaPlugin {
    @Getter private static Main instance;
    @Getter private HashUtils hashUtils;
    @Getter private StorageBackend storageBackend;
    private PluginManager manager = Bukkit.getServer().getPluginManager();

    /*
                            TODO: (Check list :P)
        * Password strength checker in register process.
        * Change password stuff.
        * Unregister.
        * Unregister for admins (unregister a target user).
        * Improve stuff.
        * Make sync settings.
        * Set user's rank via pex depending on the database (Easy rank synchronization :P)
    */

    @Override
    public void onEnable() {
        instance = this;
        hashUtils = new HashUtils();


        getCommand("aboutsettings").setExecutor(new AboutSettingsCommand(this));
        getCommand("syncsettings").setExecutor(new SyncSettingsCommand(this));
        getCommand("register").setExecutor(new RegisterCommand(this));

        storageBackend = new StorageBackendMySQL(new DatabaseCredentials("158.69.123.169", 3306, "remote", "hyuK3tF5bMHfaxY8njVs7Mj19", "webcore"));
        //storageBackend = new StorageBackendMySQL(new DatabaseCredentials("localhost", 3306, "root", "sergivb01", "webcore"));

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