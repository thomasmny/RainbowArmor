package de.eintosti.rainbowarmor;

import de.eintosti.rainbowarmor.command.RainbowArmorCommand;
import de.eintosti.rainbowarmor.listener.InventoryClick;
import de.eintosti.rainbowarmor.listener.PlayerInteract;
import de.eintosti.rainbowarmor.listener.PlayerQuit;
import de.eintosti.rainbowarmor.manager.RainbowArmorManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author einTosti
 */
public class RainbowArmor extends JavaPlugin {
    private String armorLore;
    private String armorEnabled;
    private String armorDisabled;
    private String configReloaded;
    private String prefix;
    private String noPermission;
    private Boolean oneColour;

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
        loadMessages();

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
        new InventoryClick(this);
        new PlayerInteract(this);
        new PlayerQuit(this);
    }

    public void loadMessages() {
        this.armorLore = this.getConfig().isString("armor_lore") ? this.getConfig().getString("armor_lore").replace("&", "§") : "§4R§ca§6i§en§ab§2o§bw";
        this.armorEnabled = this.getConfig().isString("armor_enabled") ? this.getConfig().getString("armor_enabled").replace("&", "§") : "§7Your armor is §anow §7colourful.";
        this.armorDisabled = this.getConfig().isString("armor_disabled") ? this.getConfig().getString("armor_disabled").replace("&", "§") : "§7Your armor is §cno longer §7colourful.";
        this.configReloaded = this.getConfig().isString("config_reloaded") ? this.getConfig().getString("config_reloaded").replace("&", "§") : "§7The config was reloaded.";
        this.prefix = this.getConfig().isString("prefix") ? this.getConfig().getString("prefix").replace("&", "§") : "§7● §4R§ca§6i§en§ab§2o§bw§3A§9r§5m§do§fr §8» ";
        this.noPermission = this.getConfig().isString("no_permissions") ? this.getConfig().getString("no_permissions").replace("&", "§") : "§7No permissions!";
        this.oneColour = !this.getConfig().isBoolean("armor_sameColour") || this.getConfig().getBoolean("armor_sameColour");
    }

    public void sendPermissionMessage(Player player) {
        player.sendMessage(this.prefix + this.noPermission);
    }

    public String getArmorLore() {
        return this.armorLore;
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

    public Boolean getOneColour() {
        return this.oneColour;
    }

    public RainbowArmorManager getRainbowArmorManager() {
        return this.rainbowArmorManager;
    }
}
