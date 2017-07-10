package net.veilmc.webcore.backend;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.veilmc.webcore.backend.connection.ConnectionPoolManager;
import org.bukkit.entity.Player;

import java.util.HashMap;

public interface StorageBackend {

    ConnectionPoolManager getPoolManager();
    
    void closeConnections();

    boolean checkProfile(Player player);

    boolean checkRegistered(Player player);

    void createProfile(Player player);

    void registerPlayer(Player player, String passwd);

    void saveSettings(Player player);

}