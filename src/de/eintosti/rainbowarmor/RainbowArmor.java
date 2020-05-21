package de.eintosti.rainbowarmor;

import de.eintosti.rainbowarmor.command.RainbowArmorCommand;
import de.eintosti.rainbowarmor.listener.InventoryClickListener;
import de.eintosti.rainbowarmor.listener.PlayerChangedWorldListener;
import de.eintosti.rainbowarmor.listener.PlayerDropItemListener;
import de.eintosti.rainbowarmor.listener.PlayerQuitListener;
import de.eintosti.rainbowarmor.manager.RainbowArmorManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author einTosti
 */
public class RainbowArmor extends JavaPlugin {
    private String armorEnabled;
    private String armorDisabled;
    private String configReloaded;
    private String prefix;
    private String noPermission;
    private String worldDisabled;
    private List<String> disabledWorlds;

    public ArrayList<UUID> enabledPlayers = new ArrayList<>();
    public HashMap<UUID, ItemStack[]> playerArmor = new HashMap<>();

    private RainbowArmorManager rainbowArmorManager;

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        initClasses();
        registerCommands();
        registerListeners();
        loadConfigSettings();

        Bukkit.getConsoleSender().sendMessage("RainbowArmor » Plugin §aenabled§r!");
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(pl -> {
            if (this.playerArmor.containsKey(pl.getUniqueId())) {
                this.rainbowArmorManager.removeColourArmor(pl);
                this.rainbowArmorManager.resetPlayerArmor(pl);
            }
        });
        Bukkit.getConsoleSender().sendMessage("RainbowArmor » Plugin §cdisabled§r!");
    }

    private void initClasses() {
        this.rainbowArmorManager = new RainbowArmorManager(this);
    }

    private void registerCommands() {
        new RainbowArmorCommand(this);
    }

    private void registerListeners() {
        new InventoryClickListener(this);
        new PlayerChangedWorldListener(this);
        new PlayerDropItemListener(this);
        new PlayerInteractListener(this);
        new PlayerQuitListener(this);
    }

    public void loadConfigSettings() {
        this.armorEnabled = this.getConfig().isString("messages.armor_enabled") ? this.getConfig().getString("messages.armor_enabled").replace("&", "§") : "§7Your armor is §anow §7colourful.";
        this.armorDisabled = this.getConfig().isString("messages.armor_disabled") ? this.getConfig().getString("messages.armor_disabled").replace("&", "§") : "§7Your armor is §cno longer §7colourful.";
        this.configReloaded = this.getConfig().isString("messages.config_reloaded") ? this.getConfig().getString("messages.config_reloaded").replace("&", "§") : "§7The config was reloaded.";
        this.prefix = this.getConfig().isString("messages.prefix") ? this.getConfig().getString("messages.prefix").replace("&", "§") : "§7● §4R§ca§6i§en§ab§2o§bw§3A§9r§5m§do§fr §8» ";
        this.noPermission = this.getConfig().isString("messages.no_permissions") ? this.getConfig().getString("messages.no_permissions").replace("&", "§") : "§7No permissions!";
        this.worldDisabled = this.getConfig().isString("messages.world_disabled") ? this.getConfig().getString("messages.world_disabled").replace("&", "§") : "§cCommand disabled in this world.";
        this.disabledWorlds = this.getConfig().isList("disabled_worlds") ? this.getConfig().getStringList("disabled_worlds") : new ArrayList<>();
    }

    public void sendPermissionMessage(Player player) {
        player.sendMessage(this.prefix + this.noPermission);
    }

    public String getArmorEnabled() {
        return this.prefix + this.armorEnabled;
    }

    public String getArmorDisabled() {
        return this.prefix + this.armorDisabled;
    }

    public String getConfigReloaded() {
        return this.prefix + this.configReloaded;
    }

    public String getWorldDisabled() {
        return this.prefix + this.worldDisabled;
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public RainbowArmorManager getRainbowArmorManager() {
        return this.rainbowArmorManager;
    }
}
