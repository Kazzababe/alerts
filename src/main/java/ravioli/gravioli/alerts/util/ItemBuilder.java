package ravioli.gravioli.alerts.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final Material material;
    private final int amount;
    private List<String> lore = new ArrayList<>();
    private String displayName;

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public ItemBuilder setLore(String... lore) {
        return this.setLore(Arrays.asList(lore));
    }

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;

        return this;
    }

    public ItemBuilder addLore(String... lore) {
        return this.addLore(Arrays.asList(lore));
    }

    public ItemBuilder addLore(List<String> lore) {
        this.lore.addAll(lore);

        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;

        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(this.material, this.amount);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            throw new RuntimeException("Error generating item, item has no ItemMeta.");
        }
        if (this.displayName != null) {
            itemMeta.setDisplayName(this.displayName);
        }
        if (!this.lore.isEmpty()) {
            itemMeta.setLore(this.lore);
        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
