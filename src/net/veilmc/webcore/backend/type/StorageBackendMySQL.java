package net.veilmc.webcore.backend.type;

import net.veilmc.webcore.Main;
import net.veilmc.webcore.backend.DatabaseCredentials;
import net.veilmc.webcore.backend.StorageBackend;
import net.veilmc.webcore.backend.connection.ConnectionPoolManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.UUID;

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
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement("INSERT INTO `hcf_statistics` (`player_name`, `player_uuid`) VALUES (?, ?)");
                    statement.setString(1, player.getName());
                    statement.setString(2, player.getUniqueId().toString());
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
    public void saveProfile(Player player) {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;
                try {
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement("UPDATE `hcf_statistics` SET `player_name`=?, `playtime`=?, `faction_name`=?, `kills`=?, `deaths`=?, `balance`=?, `lives`=?, `diamonds_mined`=?, `emeralds_mined`=?, `gold_mined`=?, `redstone_mined`=?, `lapis_mined`=?, `iron_mined`=?, `coal_mined`=? WHERE `player_uuid`=?");
                    statement.setString(1, player.getName());
                    statement.setLong(2, 515615);
                    statement.setString(3, "adw");
                    statement.setInt(4, 1);
                    statement.setInt(5, 1);
                    statement.setInt(6, 1);
                    statement.setInt(7, 1);
                    statement.setInt(8, player.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
                    statement.setInt(9, player.getStatistic(Statistic.MINE_BLOCK, Material.EMERALD_ORE));
                    statement.setInt(10, player.getStatistic(Statistic.MINE_BLOCK, Material.GOLD_ORE));
                    statement.setInt(11, player.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
                    statement.setInt(12, player.getStatistic(Statistic.MINE_BLOCK, Material.LAPIS_ORE));
                    statement.setInt(13, player.getStatistic(Statistic.MINE_BLOCK, Material.IRON_ORE));
                    statement.setInt(14, player.getStatistic(Statistic.MINE_BLOCK, Material.COAL_ORE));
                    statement.setString(15, player.getUniqueId().toString());
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

    public void insertKill(Player killer, Player dead) {
        new BukkitRunnable() {
            public void run() {
                Connection connection = null;
                PreparedStatement statement = null;

                try {
                    connection = StorageBackendMySQL.this.poolManager.getConnection();
                    statement = connection.prepareStatement("INSERT INTO `hcf_kills` (`killer_name`, `killer_uuid`, `killer_faction`, `dead_name`, `dead_uuid`, `dead_faction`) VALUES (?, ?, ?, ?, ?, ?)");

                    if(killer == null) {
                        statement.setString(1, "");
                        statement.setString(2, "environment");
                        statement.setString(3, "");
                    } else {
                        statement.setString(1, killer.getName());
                        statement.setString(2, killer.getUniqueId().toString());
                        statement.setString(3, "wad");
                    }
                    statement.setString(4, dead.getName());
                    statement.setString(5, dead.getUniqueId().toString());
                    statement.setString(6, "wado");
                    statement.executeUpdate();
                    statement.close();
                }
                catch (SQLException e) {
                    Main.getInstance().getLogger().severe("Failed insertKill -> " + killer.getName() + " & " + dead.getName());
                    e.printStackTrace();
                    this.cancel();
                }
                finally {
                    poolManager.close(connection, statement, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    @Override
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
                    preparedStatement.setTimestamp(2, deathBan.getDeathTime());
                    preparedStatement.setTimestamp(3, deathBan.getExpireTime());
                    preparedStatement.setString(4, LocationUtils.getString(deathBan.getDeathPoint()));
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    poolManager.close(connection, preparedStatement, null);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    @Override
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

    @Override
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
                        DeathBan deathBan = new DeathBan(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("player_uuid"))), resultSet.getTimestamp("death_time"), resultSet.getTimestamp("expiration"), LocationUtils.getLocation(resultSet.getString("death_point")));
                        Main.getInstance().getDeathBanManager().getDeathBanned().put(deathBan.getDead().getUniqueId(), deathBan);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    poolManager.close(connection, preparedStatement, resultSet);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    @Override
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
    }

}