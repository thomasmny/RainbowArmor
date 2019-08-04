package de.eintosti.rainbowarmor.command;

import com.sun.istack.internal.logging.Logger;
import de.eintosti.rainbowarmor.RainbowArmor;
import de.eintosti.rainbowarmor.manager.RainbowArmorManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * @author einTosti
 */
public class RainbowArmorCommand implements CommandExecutor {
    private RainbowArmor plugin;
    private RainbowArmorManager rainbowArmorManager;

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
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!player.hasPermission("rainbowArmor.reloadConfig")) {
                        plugin.sendPermissionMessage(player);
                        return true;
                    }
                    plugin.reloadConfig();
                    player.sendMessage(plugin.getConfigReloaded());
                }
                break;
            default:
                if (!plugin.enabledPlayers.contains(player.getUniqueId())) {
                    plugin.enabledPlayers.add(player.getUniqueId());
                    plugin.playerArmor.put(player.getUniqueId(), player.getEquipment().getArmorContents());
                    rainbowArmorManager.startArmorColouring(player);
                    player.sendMessage(plugin.getArmorEnabled());
                } else {
                    plugin.enabledPlayers.remove(player.getUniqueId());
                    rainbowArmorManager.removeColourArmor(player);
                    rainbowArmorManager.resetPlayerArmor(player);
                    player.sendMessage(plugin.getArmorDisabled());
                }
                break;
        }
        return true;
    }
}
