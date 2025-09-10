package cc.zyrn.duels.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private ItemStack stack;

    private int amount;
    private String name;
    private List<String> lore;
    private Material material;

    private boolean clearEnchants;

    private int customModelData;

    private boolean glow;
    private Map<Enchantment, Integer> map;

    public ItemBuilder(Material material) {
        this.amount = 1;
        this.customModelData = -1;
        this.material = material;
    }

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public ItemBuilder clearEnchants() {
        this.clearEnchants = true;
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (map == null)
            map = new HashMap<>();

        map.put(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        if (map == null || !map.containsKey(enchantment))
            return this;

        map.remove(enchantment);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemStack getStack() {
        final ItemStack itemStack = stack == null ? new ItemStack(material, amount) : stack;
        final ItemMeta meta = itemStack.getItemMeta();

        if (meta == null)
            return itemStack;

        if (name != null)
            meta.setDisplayName(CC.translate(name));

        if (lore != null)
            meta.setLore(CC.translate(lore));

        if (map != null && !map.isEmpty()) {
            if (clearEnchants && meta.hasEnchants())
                meta.removeEnchantments();

            map.forEach((enchantment, integer) -> meta.addEnchant(enchantment, integer, true));
        }

        if (glow && map == null) {
            meta.addEnchant(Enchantment.FIRE_PROTECTION, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        if (meta.hasCustomModelData() && customModelData == -1)
            this.customModelData = meta.getCustomModelData();

        if (customModelData != -1)
            meta.setCustomModelData(customModelData);


        return itemStack;
    }

}
