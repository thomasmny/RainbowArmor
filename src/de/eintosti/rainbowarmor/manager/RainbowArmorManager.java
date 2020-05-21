package de.eintosti.rainbowarmor.manager;

import de.eintosti.rainbowarmor.RainbowArmor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * @author einTosti
 */
public class RainbowArmorManager {
    private final RainbowArmor plugin;

    private int r = 255;
    private int g = 0;
    private int b = 0;

    public RainbowArmorManager(RainbowArmor plugin) {
        this.plugin = plugin;
    }

    public void startArmorColouring(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (plugin.enabledPlayers.contains(player.getUniqueId())) {
                Color color = nextColor();

                dyeArmor(playerInventory.getHelmet(), color);
                dyeArmor(playerInventory.getChestplate(), color);
                dyeArmor(playerInventory.getLeggings(), color);
                dyeArmor(playerInventory.getBoots(), color);
            }
        }, 0L, 1L);
    }

    public Color nextColor() {
        nextRGB();
        return Color.fromRGB(r, g, b);
    }

    private void nextRGB() {
        if (r == 255 && g < 255 && b == 0) {
            g += 1;
        }
        if (g == 255 && r > 0 && b == 0) {
            r -= 1;
        }
        if (g == 255 && b < 255 && r == 0) {
            b += 1;
        }
        if (b == 255 && g > 0 && r == 0) {
            g -= 1;
        }
        if (b == 255 && r < 255 && g == 0) {
            r += 1;
        }
        if (r == 255 && b > 0 && g == 0) {
            b -= 1;
        }
    }

    private void dyeArmor(ItemStack itemStack, Color color) {
        if (itemStack == null) return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof LeatherArmorMeta)) return;

        ((LeatherArmorMeta) itemMeta).setColor(color);
        itemStack.setItemMeta(itemMeta);
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
