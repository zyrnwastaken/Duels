package cc.zyrn.duels.profile.data;

import cc.zyrn.duels.util.ItemStackSerializer;
import cc.zyrn.duels.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ProfileData {

    private final Location location;
    private final ItemStack[] contents, armorContents;
    private final double health;
    private final int food, air;

    public ProfileData(Player player) {
        this.location = player.getLocation();
        this.contents = player.getInventory().getContents();
        this.armorContents = player.getInventory().getArmorContents();

        this.health = player.getHealth();
        this.food = player.getFoodLevel();
        this.air = player.getRemainingAir();
    }

    public ProfileData(String s) {
        String[] args = s.split("|||");

        this.location = LocationUtil.STL(args[0]);
        this.contents = ItemStackSerializer.convertStringToItemStackArray(args[1]);
        this.armorContents = ItemStackSerializer.convertStringToItemStackArray(args[2]);
        this.health = Double.valueOf(args[3]);
        this.food = Integer.valueOf(args[4]);
        this.air = Integer.valueOf(args[5]);
    }

    public final void apply(Player player) {
        player.teleport(location);
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armorContents);
        player.setHealth(health);
        player.setFoodLevel(food);
        player.setRemainingAir(air);
    }

    @Override
    public String toString() {
        return LocationUtil.LTS(location) + "|||" +
                ItemStackSerializer.convertItemStackArrayToString(contents) + "|||" +
                ItemStackSerializer.convertItemStackArrayToString(armorContents) + "|||" +
                health + "|||" + food + "|||" + air;
    }
}
