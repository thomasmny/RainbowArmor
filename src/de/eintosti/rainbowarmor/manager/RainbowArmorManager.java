package de.eintosti.rainbowarmor.manager;

import de.eintosti.rainbowarmor.RainbowArmor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

/**
 * @author einTosti
 */
public class RainbowArmorManager {
    private RainbowArmor plugin;

    private Random randonNumber = new Random();
    private int taskId;
    private final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

    public boolean[] colourfulArmor = new boolean[4];

    public RainbowArmorManager(RainbowArmor plugin) {
        this.plugin = plugin;
    }

    private Color randomColor() {
        return Color.fromRGB(this.randonNumber.nextInt(255), this.randonNumber.nextInt(255), this.randonNumber.nextInt(255));
    }

    public void wearColourArmor(PlayerInteractEvent event, Material colorArmor) {
        Player player = event.getPlayer();
        PlayerInventory playerInv = player.getInventory();

        BukkitTask scheduledTask = this.scheduler.runTaskTimerAsynchronously(plugin, () -> {
            ItemStack playerArmorSlot = null;

            switch (colorArmor) {
                case LEATHER_HELMET:
                    playerArmorSlot = playerInv.getHelmet();
                    break;
                case LEATHER_CHESTPLATE:
                    playerArmorSlot = playerInv.getChestplate();
                    break;
                case LEATHER_LEGGINGS:
                    playerArmorSlot = playerInv.getLeggings();
                    break;
                case LEATHER_BOOTS:
                    playerArmorSlot = playerInv.getBoots();
                    break;
            }

            if (playerArmorSlot != null) {
                ItemMeta armorMeta = playerArmorSlot.getItemMeta();
                if (armorMeta instanceof LeatherArmorMeta) {
                    ((LeatherArmorMeta) armorMeta).setColor(randomColor());
                    playerArmorSlot.setItemMeta(armorMeta);
                }
            }
        }, 5L, 5L);
        this.taskId = scheduledTask.getTaskId();
    }

    public void removeColourArmor(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setHelmet(null);
        playerInventory.setChestplate(null);
        playerInventory.setLeggings(null);
        playerInventory.setBoots(null);
    }

    public void resetPlayerArmor(Player player) {
        player.getInventory().setArmorContents(plugin.playerArmor.get(player.getUniqueId()));
    }

    public void startArmorColouring(Player player) {
        Boolean oneColour = plugin.getOneColour();
        PlayerInventory playerInv = player.getInventory();

        playerInv.setHelmet(getItemStack(Material.LEATHER_HELMET));
        playerInv.setChestplate(getItemStack(Material.LEATHER_CHESTPLATE));
        playerInv.setLeggings(getItemStack(Material.LEATHER_LEGGINGS));
        playerInv.setBoots(getItemStack(Material.LEATHER_BOOTS));

        ItemMeta helmetMeta = playerInv.getHelmet().getItemMeta();
        ItemMeta chestplateMeta = playerInv.getHelmet().getItemMeta();
        ItemMeta leggingsMeta = playerInv.getHelmet().getItemMeta();
        ItemMeta bootsMeta = playerInv.getHelmet().getItemMeta();

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (plugin.enabledPlayers.contains(player.getUniqueId())) {
                if (oneColour) {
                    Color armorColour = randomColor();
                    setArmorPieceColour(helmetMeta, armorColour, playerInv.getHelmet());
                    setArmorPieceColour(chestplateMeta, armorColour, playerInv.getChestplate());
                    setArmorPieceColour(leggingsMeta, armorColour, playerInv.getLeggings());
                    setArmorPieceColour(bootsMeta, armorColour, playerInv.getBoots());
                } else {
                    setArmorPieceColour(helmetMeta, randomColor(), playerInv.getHelmet());
                    setArmorPieceColour(chestplateMeta, randomColor(), playerInv.getChestplate());
                    setArmorPieceColour(leggingsMeta, randomColor(), playerInv.getLeggings());
                    setArmorPieceColour(bootsMeta, randomColor(), playerInv.getBoots());
                }
            }
        }, 0L, 10L);
    }

    private void setArmorPieceColour(ItemMeta armorMeta, Color armorColour, ItemStack armorSlot) {
        ((LeatherArmorMeta) armorMeta).setColor(armorColour);
        armorSlot.setItemMeta(armorMeta);
    }

    public void stopArmorColouring() {
        this.scheduler.cancelTask(this.taskId);
        this.colourfulArmor[0] = false;
        this.colourfulArmor[1] = false;
        this.colourfulArmor[2] = false;
        this.colourfulArmor[3] = false;
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
