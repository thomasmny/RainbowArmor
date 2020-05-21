package de.eintosti.rainbowarmor.listener;

import de.eintosti.rainbowarmor.RainbowArmor;
import de.eintosti.rainbowarmor.manager.RainbowArmorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

/**
 * @author einTosti
 */
public class PlayerChangedWorldListener implements Listener {
    private final RainbowArmor plugin;
    private final RainbowArmorManager rainbowArmorManager;

    public PlayerChangedWorldListener(RainbowArmor plugin) {
        this.plugin = plugin;
        this.rainbowArmorManager = plugin.getRainbowArmorManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (!plugin.enabledPlayers.contains(player.getUniqueId())) return;
        if (plugin.getDisabledWorlds().stream().anyMatch(s -> s.equalsIgnoreCase(player.getWorld().getName()))) {
            plugin.enabledPlayers.remove(player.getUniqueId());
            rainbowArmorManager.removeColourArmor(player);
            rainbowArmorManager.resetPlayerArmor(player);
            player.sendMessage(plugin.getWorldDisabled());
        }
    }
}
