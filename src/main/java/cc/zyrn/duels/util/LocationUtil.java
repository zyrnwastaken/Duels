package cc.zyrn.duels.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {

    public static String LTS(Location location) {
        if (location == null)
            return "null";

        return location.getWorld() + "//" + location.getX() + "//" + location.getY() + "//" + location.getZ() + "//"
                + location.getYaw() + "//" + location.getPitch();
    }

    public static Location STL(String string) {
        if (string.equalsIgnoreCase("null"))
            return null;

        final String[] args = string.split("//");
        return new Location(Bukkit.getWorld(args[0]), Double.parseDouble(args[1]),
                Double.parseDouble(args[2]), Double.parseDouble(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]));
    }

}
