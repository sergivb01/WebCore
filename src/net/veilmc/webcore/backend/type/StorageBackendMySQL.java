package net.veilmc.webcore.backend.type;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.sergivb01.base.BasePlugin;
import me.sergivb01.base.user.BaseUser;
import net.veilmc.webcore.Main;
import net.veilmc.webcore.backend.DatabaseCredentials;
import net.veilmc.webcore.backend.StorageBackend;
import net.veilmc.webcore.backend.connection.ConnectionPoolManager;
import net.veilmc.webcore.utils.PermissionsExUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class StorageBackendMySQL implements StorageBackend {

    private ConnectionPoolManager poolManager;

    public StorageBackendMySQL(DatabaseCredentials credentials) {
        this.poolManager = new ConnectionPoolManager(Main.getInstance(), credentials);
    }

    @Override
    public ConnectionPoolManager getPoolManager() {
        return poolManager;
    }

    @Override
    public void closeConnections() {
        this.poolManager.closePool();
    }

    @Override
    public void createProfile(Player player) {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;

                try {
                    BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId());
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement("INSERT INTO `player_credentials` (`player_name`, `player_uuid`, `password`) VALUES (?, ?, ?)");
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getUniqueId().toString());
                    statement.setString(3, "");
                    statement.executeUpdate();
                    statement.close();

                    statement = connection.prepareStatement("INSERT INTO `player_settings` (`player_uuid`, `privatemessages`, `sounds`, `globalchat`, `staffchat`, `staffchatview`, `staffscoreboard`, `rank`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    statement.setString(1, player.getUniqueId().toString());
                    statement.setBoolean(2, baseUser.isMessagesVisible());
                    statement.setBoolean(3, baseUser.isMessagingSounds());
                    statement.setBoolean(4, baseUser.isGlobalChatVisible());
                    statement.setBoolean(5, baseUser.isInStaffChat());
                    statement.setBoolean(6, baseUser.isStaffChatVisible());
                    statement.setBoolean(7, baseUser.isGlintEnabled());
                    statement.setString(8, PermissionsExUtils.getRanks(player));
                    statement.executeUpdate();
                    statement.close();
                }
                catch (SQLException e) {
                    if (!e.getMessage().toLowerCase().contains("duplicate") && !e.getMessage().toLowerCase().contains("not unique")) {
                        Main.getInstance().getLogger().severe("Failed createProfile -> " + player.getName());
                        e.printStackTrace();
                        this.cancel();
                    }
                }
                finally {
                    poolManager.close(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    @Override
    public boolean checkProfile(Player player){
        boolean isreg = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = poolManager.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM `player_credentials` WHERE `player_uuid`='" + player.getUniqueId().toString() + "'");
            resultSet = preparedStatement.executeQuery();

            if(resultSet.isBeforeFirst()) {
                isreg = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            poolManager.close(connection, preparedStatement, resultSet);
        }

        return isreg;
    }

    @Override
    public boolean checkRegistered(Player player){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = poolManager.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM `player_credentials` WHERE `player_uuid`='" + player.getUniqueId().toString() + "'");
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return resultSet.getString("password").isEmpty() || resultSet.getString("password").equals("");
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            poolManager.close(connection, preparedStatement, resultSet);
        }
        return true;
    }

    @Override
    public HashMap<String, Boolean> getData(Player player){
        HashMap<String, Boolean> settings = new HashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = poolManager.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM `player_settings` WHERE `player_uuid`='" + player.getUniqueId().toString() + "'");
            resultSet = preparedStatement.executeQuery();

            settings.put("privatemessages", resultSet.getBoolean("privatemessages"));
            settings.put("sounds", resultSet.getBoolean("sounds"));
            settings.put("globalchat", resultSet.getBoolean("globalchat"));
            settings.put("staffchat", resultSet.getBoolean("staffchat"));
            settings.put("staffchatview", resultSet.getBoolean("staffchatview"));
            settings.put("staffscoreboard", resultSet.getBoolean("staffscoreboard"));

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            poolManager.close(connection, preparedStatement, resultSet);
        }

        return settings;
    }

    @Override
    public void registerPlayer(Player player, String passwd){
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;

                try {
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement("UPDATE `player_credentials` SET `player_name`=?, `password`=? WHERE `player_uuid`='" + player.getUniqueId().toString() + "'");
                    statement.setString(1, player.getName());
                    statement.setString(2, passwd);
                    statement.executeUpdate();
                    statement.close();
                }
                catch (SQLException e) {
                    if (!e.getMessage().toLowerCase().contains("duplicate") && !e.getMessage().toLowerCase().contains("not unique")) {
                        Main.getInstance().getLogger().severe("Failed registerUser -> " + player.getName());
                        e.printStackTrace();
                        this.cancel();
                    }
                }
                finally {
                    poolManager.close(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    @Override
    public void saveSettings(Player player) {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                try {
                    BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId());
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement("UPDATE `player_settings` SET `privatemessages`=?, `sounds`=?, `globalchat`=?, `staffchat`=?, `staffchatview`=?, `staffscoreboard`=?, `rank`=? WHERE `player_uuid`='" + player.getUniqueId().toString() + "'");
                    statement.setBoolean(1, baseUser.isMessagesVisible());
                    statement.setBoolean(2, baseUser.isMessagingSounds());
                    statement.setBoolean(3, baseUser.isGlobalChatVisible());
                    statement.setBoolean(4, baseUser.isInStaffChat());
                    statement.setBoolean(5, baseUser.isStaffChatVisible());
                    statement.setBoolean(6, baseUser.isGlintEnabled());
                    statement.setString(7, PermissionsExUtils.getRanks(player));
                    statement.executeUpdate();
                    statement.close();
                }
                catch (SQLException e) {
                    Main.getInstance().getLogger().severe("Failed saveProfile (exception) -> " + player.getName());
                    e.printStackTrace();
                    this.cancel();
                }
                finally {
                    poolManager.close(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }


/*    @Override
    public void setDeathBanned(OfflinePlayer player, DeathBan deathBan) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = poolManager.getConnection();
                    preparedStatement = connection.prepareStatement("INSERT INTO `hcf_deathbans` (player_uuid, death_time, expiration, death_point) VALUES (?, ?, ?, ?)");
                    preparedStatement.setString(1, player.getUniqueId().toString());
                    preparedStatement.setString(2, "awdawd");
                    preparedStatement.setString(3, "awdwa");
                    preparedStatement.setString(4, "wadwad");
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    poolManager.close(connection, preparedStatement, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    //@Override
    public void removeDeathBan(OfflinePlayer player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = poolManager.getConnection();
                    preparedStatement = connection.prepareStatement("DELETE FROM `hcf_deathbans` WHERE `player_uuid`=?");
                    preparedStatement.setString(1, player.getUniqueId().toString());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    poolManager.close(connection, preparedStatement, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    //@Override
    public void loadDeathBans() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                    connection = poolManager.getConnection();
                    preparedStatement = connection.prepareStatement("SELECT * FROM `hcf_deathbans`");
                    resultSet = preparedStatement.executeQuery();

                    while(resultSet.next()) {
                        //DeathBan deathBan = new DeathBan(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("player_uuid"))), resultSet.getTimestamp("death_time"), resultSet.getTimestamp("expiration"), LocationUtils.getLocation(resultSet.getString("death_point")));
                        //Main.getInstance().getDeathBanManager().getDeathBanned().put(deathBan.getDead().getUniqueId(), deathBan);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    poolManager.close(connection, preparedStatement, resultSet);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    //@Override
    public void addLives(OfflinePlayer player, int amount) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = poolManager.getConnection();
                    preparedStatement = connection.prepareStatement("UPDATE `hcf_statistics` SET lives=lives+? WHERE player_uuid=?");
                    preparedStatement.setInt(1, amount);
                    preparedStatement.setString(2, player.getUniqueId().toString());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    poolManager.close(connection, preparedStatement, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }*/

}