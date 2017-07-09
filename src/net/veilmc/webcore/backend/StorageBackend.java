package net.veilmc.webcore.backend;

import net.veilmc.webcore.backend.connection.ConnectionPoolManager;
import org.bukkit.entity.Player;
import me.sergivb01.backend.connection.ConnectionPoolManager;
import me.sergivb01.hcf.deathban.DeathBan;

public interface StorageBackend {

    ConnectionPoolManager getPoolManager();
    
    void closeConnections();
    
    void createProfile(Player player);
    
    void saveProfile(Player player);


}