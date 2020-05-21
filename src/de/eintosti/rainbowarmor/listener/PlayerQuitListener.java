package de.eintosti.rainbowarmor.listener;

import de.eintosti.rainbowarmor.RainbowArmor;
import de.eintosti.rainbowarmor.manager.RainbowArmorManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author einTosti
 */
public class PlayerQuitListener implements Listener {
    private final RainbowArmor plugin;
    private final RainbowArmorManager rainbowArmorManager;

    public PlayerQuitListener(RainbowArmor plugin) {
        this.plugin = plugin;
        this.rainbowArmorManager = plugin.getRainbowArmorManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.enabledPlayers.remove(player.getUniqueId());
        if (plugin.playerArmor.containsKey(player.getUniqueId())) {
            rainbowArmorManager.removeColourArmor(player);
            rainbowArmorManager.resetPlayerArmor(player);
        }
    }
}
