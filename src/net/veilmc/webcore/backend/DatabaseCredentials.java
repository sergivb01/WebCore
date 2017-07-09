package net.veilmc.webcore.backend;

import lombok.Getter;

public class DatabaseCredentials {
    
    @Getter private String hostname;
    @Getter private int port;
    @Getter private String username;
    @Getter private String password;
    @Getter private String databaseName;
    
    public DatabaseCredentials(String hostname, int port, String username, String password, String databaseName) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}