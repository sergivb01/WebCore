package net.veilmc.webcore.utils;

import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsExUtils {
    public static String getRanks(Player player) {
        String message = "";
        PermissionUser permissionUser = PermissionsEx.getUser(player);
        for (String ranks : permissionUser.getGroupNames()) {
            message += ranks + ", ";
        }
        if (message.length() > 2) {
            message = message.substring(0, message.length() - 2);
        }
        if (message.length() == 0) {
            message = "User";
        }
        if (message.equalsIgnoreCase("default")) {
            message = "User";
        }
        return message;
    }
}
