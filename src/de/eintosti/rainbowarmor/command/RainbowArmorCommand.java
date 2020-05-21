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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

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

                playerInventory.setHelmet(getItemStack(Material.LEATHER_HELMET));
                playerInventory.setChestplate(getItemStack(Material.LEATHER_CHESTPLATE));
                playerInventory.setLeggings(getItemStack(Material.LEATHER_LEGGINGS));
                playerInventory.setBoots(getItemStack(Material.LEATHER_BOOTS));

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

    private ItemStack getItemStack(Material material) {
        ItemStack itemStack = new ItemStack(material, 1, (byte) 0);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName("§4R§ca§6i§en§ab§2o§bw§3A§9r§5m§do§fr");
        itemMeta.addItemFlags(ItemFlag.values());

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
