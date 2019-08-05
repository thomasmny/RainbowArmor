package de.eintosti.rainbowarmor.listener;

import de.eintosti.rainbowarmor.RainbowArmor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author einTosti
 */
public class PlayerDropItem implements Listener {
    private RainbowArmor plugin;

    public PlayerDropItem(RainbowArmor plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (plugin.enabledPlayers.contains(player.getUniqueId())) {
            ItemStack itemStack = event.getItemDrop().getItemStack();
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta.hasDisplayName()) {
                if (itemMeta.getDisplayName().equals("§4R§ca§6i§en§ab§2o§bw§3A§9r§5m§do§fr"))
                    event.setCancelled(true);
            }
        }
    }
}