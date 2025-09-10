package cc.zyrn.duels.kit;

import cc.zyrn.duels.util.ItemStackSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class Kit {

    private final String name;

    private ItemStack[] contents, armorContents;

    public Kit(String name) {
        this.name = name;
    }

    public Kit(String name, Player player) {
        this.name = name;
        this.contents = player.getInventory().getContents();
        this.armorContents = player.getInventory().getArmorContents();
    }

    public final void apply(Player player) {
        player.getInventory().clear();

        player.setHealth(20);
        player.setFoodLevel(20);

        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armorContents);
    }

    public Document toBson() {
        return new Document("_id", name)
                .append("contents", ItemStackSerializer.convertItemStackArrayToString(contents))
                .append("armorContents", ItemStackSerializer.convertItemStackArrayToString(armorContents));
    }

}
