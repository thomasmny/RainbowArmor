package de.eintosti.rainbowarmor.listener;

import de.eintosti.rainbowarmor.RainbowArmor;
import de.eintosti.rainbowarmor.manager.RainbowArmorManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author einTosti
 */
public class PlayerInteract implements Listener {
    private RainbowArmor plugin;
    private RainbowArmorManager rainbowArmorManager;

    public PlayerInteract(RainbowArmor plugin) {
        this.plugin = plugin;
        this.rainbowArmorManager = plugin.getRainbowArmorManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if ((itemStack == null) || (itemStack.getType() == Material.AIR)) {
            return;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            rainbowArmorManager.stopArmorColouring();
        }

        List<String> itemLore = itemMeta.getLore();
        if (itemLore == null) {
            rainbowArmorManager.stopArmorColouring();
            return;
        }

        if (!itemLore.contains(plugin.getArmorLore())) {
            rainbowArmorManager.stopArmorColouring();
        }
        switch (itemStack.getType()) {
            case LEATHER_HELMET:
                if (!rainbowArmorManager.colourfulArmor[0]) {
                    rainbowArmorManager.colourfulArmor[0] = true;
                    rainbowArmorManager.wearColourArmor(event, Material.LEATHER_HELMET);
                }
                break;
            case LEATHER_CHESTPLATE:
                if (!rainbowArmorManager.colourfulArmor[1]) {
                    rainbowArmorManager.colourfulArmor[1] = true;
                    rainbowArmorManager.wearColourArmor(event, Material.LEATHER_CHESTPLATE);
                }
                break;
            case LEATHER_LEGGINGS:
                if (!rainbowArmorManager.colourfulArmor[2]) {
                    rainbowArmorManager.colourfulArmor[2] = true;
                    rainbowArmorManager.wearColourArmor(event, Material.LEATHER_LEGGINGS);
                }
                break;
            case LEATHER_BOOTS:
                if (!rainbowArmorManager.colourfulArmor[3]) {
                    rainbowArmorManager.colourfulArmor[3] = true;
                    rainbowArmorManager.wearColourArmor(event, Material.LEATHER_BOOTS);
                }
                break;
        }
    }
}
