package com.extrahardmode.beardstat.perlifestats;


import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Diemex
 */
public class RareDropListener implements Listener
{
    private final ExtraStatsPlugin plugin;


    public RareDropListener(ExtraStatsPlugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    /**
     * When an entity dies check its drops to see if there are any rare drops
     */
    @EventHandler
    private void onEntityDeath(EntityDeathEvent event)
    {
        Entity entity = (Entity) event.getEntity();
        if (entity instanceof Monster && ((Monster) entity).getKiller() != null)
        {
            final Player player = ((Monster) entity).getKiller();

            for (ItemStack drop : event.getDrops())
            {
                if (!isNaturalDrop(event.getEntityType(), drop.getType()))
                {
                    plugin.getStat(player.getName(), player.getWorld().getName(), "raredrops", drop.getType().name().toLowerCase().replace("_", "")).incrementStat(drop.getAmount());
                }
            }
        }
    }


    /**
     * Determine if a drop is a normal drop
     *
     * @param type type of Monster
     * @param drop the dropped material
     *
     * @return if natural drop, or false if not, if type not a monster will return true
     */
    public boolean isNaturalDrop(EntityType type, Material drop)
    {
        switch (type)
        {
            case CREEPER:
                return drop == Material.SULPHUR;
            case SKELETON:
                return drop == Material.BOW || drop == Material.ARROW || drop == Material.BONE || isArmor(drop);
            case SPIDER:
                return drop == Material.STRING || drop == Material.SPIDER_EYE;
            case GIANT:
                return true;
            case ZOMBIE:
                return drop == Material.ROTTEN_FLESH || isArmor(drop) || isSword(drop);
            case SLIME:
                return drop == Material.SLIME_BALL;
            case GHAST:
                return drop == Material.GHAST_TEAR || drop == Material.SULPHUR;
            case PIG_ZOMBIE:
                return drop == Material.ROTTEN_FLESH || drop == Material.GOLD_NUGGET || isArmor(drop) || isSword(drop);
            case ENDERMAN:
                return drop == Material.ENDER_PEARL;
            case CAVE_SPIDER:
                return drop == Material.STRING || drop == Material.SPIDER_EYE;
            case SILVERFISH:
                return false;
            case BLAZE:
                return drop == Material.BLAZE_ROD;
            case MAGMA_CUBE:
                return drop == Material.MAGMA_CREAM;
            case ENDER_DRAGON:
                return false;
            case WITHER:
                return drop == Material.NETHER_STAR;
            case BAT:
                return false;
            case WITCH:
                return drop == Material.STICK || drop == Material.REDSTONE || drop == Material.GLOWSTONE_DUST || drop == Material.POTION;
        }
        return true;
    }


    public static boolean isArmor(Material mat)
    {
        return mat == Material.CHAINMAIL_CHESTPLATE || mat == Material.DIAMOND_CHESTPLATE || mat == Material.GOLD_CHESTPLATE || mat == Material.IRON_CHESTPLATE || mat == Material.LEATHER_CHESTPLATE
                || mat == Material.CHAINMAIL_LEGGINGS || mat == Material.DIAMOND_LEGGINGS || mat == Material.GOLD_LEGGINGS || mat == Material.IRON_LEGGINGS || mat == Material.LEATHER_LEGGINGS
                || mat == Material.CHAINMAIL_HELMET || mat == Material.DIAMOND_HELMET || mat == Material.GOLD_HELMET || mat == Material.IRON_HELMET || mat == Material.LEATHER_HELMET
                || mat == Material.CHAINMAIL_BOOTS || mat == Material.DIAMOND_BOOTS || mat == Material.GOLD_BOOTS || mat == Material.IRON_BOOTS || mat == Material.LEATHER_BOOTS;
    }


    public static boolean isSword(Material mat)
    {
        return mat == Material.DIAMOND_SWORD || mat == Material.GOLD_SWORD || mat == Material.IRON_SWORD || mat == Material.STONE_SWORD || mat == Material.WOOD_SWORD;
    }
}
