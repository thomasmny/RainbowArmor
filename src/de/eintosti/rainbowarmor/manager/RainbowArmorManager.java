package de.eintosti.rainbowarmor.manager;

import de.eintosti.rainbowarmor.RainbowArmor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * @author einTosti
 */
public class RainbowArmorManager {
    private final RainbowArmor plugin;

    private int r = 255, g = 0, b = 0;

    private int posRed, negRed;
    private int posGreen, negGreen;
    private int posBlue, negBlue;

    public RainbowArmorManager(RainbowArmor plugin) {
        this.plugin = plugin;
    }

    public void startArmorColouring(Player player) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (plugin.enabledPlayers.contains(player.getUniqueId())) {
                nextColor(player);
            }
        }, 1L, 1L);
    }

    public void nextColor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        Color color = nextRGB();

        playerInventory.setHelmet(getItemStack(Material.LEATHER_HELMET, color));
        playerInventory.setChestplate(getItemStack(Material.LEATHER_CHESTPLATE, color));
        playerInventory.setLeggings(getItemStack(Material.LEATHER_LEGGINGS, color));
        playerInventory.setBoots(getItemStack(Material.LEATHER_BOOTS, color));
    }

    private Color nextRGB() {
        int increment = 15;
        int max = 255 / increment;

        if (posGreen <= max) {
            ++this.posGreen;
            this.g = (posGreen - 1) * increment;
        } else if (negRed <= max) {
            this.negRed++;
            this.r = 255 - increment * (negRed - 1);
        } else if (posBlue <= max) {
            ++this.posBlue;
            this.b = (posBlue - 1) * increment;
        } else if (negGreen <= max) {
            ++this.negGreen;
            this.g = 255 - increment * (negGreen - 1);
        } else if (posRed <= max) {
            ++this.posRed;
            this.r = (posRed - 1) * increment;
        } else if (negBlue <= max) {
            ++this.negBlue;
            this.b = 255 - increment * (negBlue - 1);
        } else {
            this.posRed = 0;
            this.negRed = 0;
            this.posGreen = 0;
            this.negGreen = 0;
            this.posBlue = 0;
            this.negBlue = 0;
        }

        return Color.fromRGB(r, g, b);
    }

    public ItemStack getItemStack(Material material, Color color) {
        ItemStack itemStack = new ItemStack(material, 1, (byte) 0);
        ItemMeta itemMeta = itemStack.getItemMeta();

        ((LeatherArmorMeta) itemMeta).setColor(color);
        itemMeta.setDisplayName("§4R§ca§6i§en§ab§2o§bw§3A§9r§5m§do§fr");
        itemMeta.addItemFlags(ItemFlag.values());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getItemStack(Material material) {
        return getItemStack(material, Color.fromRGB(r, g, b));
    }

    public void removeColourArmor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setHelmet(null);
        playerInventory.setChestplate(null);
        playerInventory.setLeggings(null);
        playerInventory.setBoots(null);
    }

    public void resetPlayerArmor(Player player) {
        player.getInventory().setArmorContents(plugin.playerArmor.get(player.getUniqueId()));
    }
}
