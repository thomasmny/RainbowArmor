package de.eintosti.rainbowarmor.command;

import com.sun.istack.internal.logging.Logger;
import de.eintosti.rainbowarmor.RainbowArmor;
import de.eintosti.rainbowarmor.manager.RainbowArmorManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.logging.Level;

/**
 * @author einTosti
 */
public class RainbowArmorCommand implements CommandExecutor {
    private final RainbowArmor plugin;
    private final RainbowArmorManager rainbowArmorManager;

    public RainbowArmorCommand(RainbowArmor plugin) {
        this.plugin = plugin;
        this.rainbowArmorManager = plugin.getRainbowArmorManager();
        Bukkit.getPluginCommand("RainbowArmor").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Logger.getLogger(RainbowArmorCommand.class).log(Level.INFO, "You have to be a player to use this command!");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("rainbowArmor.use")) {
            plugin.sendPermissionMessage(player);
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission("rainbowArmor.reloadConfig")) {
                    plugin.sendPermissionMessage(player);
                    return true;
                }
                plugin.reloadConfig();
                plugin.loadConfigSettings();
                player.sendMessage(plugin.getConfigReloaded());
            }
        } else {
            if (plugin.getDisabledWorlds().stream().anyMatch(s -> s.equalsIgnoreCase(player.getWorld().getName()))) {
                player.sendMessage(plugin.getWorldDisabled());
                return true;
            }

            if (!plugin.enabledPlayers.contains(player.getUniqueId())) {
                plugin.enabledPlayers.add(player.getUniqueId());
                plugin.playerArmor.put(player.getUniqueId(), player.getEquipment().getArmorContents());

                PlayerInventory playerInventory = player.getInventory();

                playerInventory.setHelmet(rainbowArmorManager.getItemStack(Material.LEATHER_HELMET));
                playerInventory.setChestplate(rainbowArmorManager.getItemStack(Material.LEATHER_CHESTPLATE));
                playerInventory.setLeggings(rainbowArmorManager.getItemStack(Material.LEATHER_LEGGINGS));
                playerInventory.setBoots(rainbowArmorManager.getItemStack(Material.LEATHER_BOOTS));

                rainbowArmorManager.startArmorColouring(player);
                player.sendMessage(plugin.getArmorEnabled());
            } else {
                plugin.enabledPlayers.remove(player.getUniqueId());
                rainbowArmorManager.removeColourArmor(player);
                rainbowArmorManager.resetPlayerArmor(player);
                player.sendMessage(plugin.getArmorDisabled());
            }
        }
        return true;
    }
}
