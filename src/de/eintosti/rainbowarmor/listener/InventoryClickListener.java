package de.eintosti.rainbowarmor.listener;

import de.eintosti.rainbowarmor.RainbowArmor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * @author einTosti
 */
public class InventoryClickListener implements Listener {
    private final RainbowArmor plugin;

    public InventoryClickListener(RainbowArmor plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (plugin.enabledPlayers.contains(player.getUniqueId())) {
            if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
                event.setCancelled(true);
            }
        }
    }
}
