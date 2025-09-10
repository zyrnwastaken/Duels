package cc.zyrn.duels.util;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CC {

    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> translate(List<String> s) {
        return s.stream().map(CC::translate).collect(Collectors.toList());
    }

    public static String[] translate(String[] args) {
        return translate(Arrays.stream(args).toList()).toArray(new String[0]);
    }

}
