package com.extrahardmode.beardstat.perlifestats;


import com.extrahardmode.module.EntityHelper;
import com.tehbeard.BeardStat.containers.EntityStatBlob;
import com.tehbeard.BeardStat.containers.IStat;
import com.tehbeard.BeardStat.containers.StatVector;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Iterator;

/**
 * @author Diemex
 */
public class PerLifeListener implements Listener
{
    private final String current = "current";

    private final String maximum = "max";

    /**
     * Kills without the player taken any damage
     */
    private final String nodmg = "nodmg";
    private final ExtraStatsPlugin plugin;


    public PerLifeListener(ExtraStatsPlugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    /**
     * When a mob is killed by a player
     */
    @EventHandler
    private void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Monster)
        {
            //Check if the mob was killed with the player having an unfair advantage
            if (plugin.getAwesomeEhm() == null || !EntityHelper.isLootLess(entity)) //only check if ehm loaded
                if (event.getEntity().getKiller() != null)
                {
                    Player player = event.getEntity().getKiller();

                    //Normal kills
                    plugin.incrWithTotal(player.getName(), player.getWorld().getName(), current + "kills", maximum + "kills", event.getEntityType().name().toLowerCase().replace("_", ""));
                    //Total Kills
                    plugin.incrWithTotal(player.getName(), player.getWorld().getName(), current + "kills", maximum + "kills", "total");

                    //NoDmgKills
                    plugin.incrWithTotal(player.getName(), player.getWorld().getName(), current + nodmg + "kills", maximum + nodmg + "kills", event.getEntityType().name().toLowerCase().replace("_", ""));
                    plugin.incrWithTotal(player.getName(), player.getWorld().getName(), current + nodmg + "kills", maximum + nodmg + "kills", "total");
                }
        }
    }


    /**
     * When a Player takes damage clear his current nodmgkill statistics
     */
    @EventHandler
    private void onPlayerTakeDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            final Player player = (Player) event.getEntity();
            //Reset all current stats for this life
            EntityStatBlob statBlob = plugin.getPlayer(player.getName());
            StatVector stats = statBlob.getStats(ExtraStatsPlugin.ourDomain, "*", current + nodmg + "[a-zA-Z0-9_]*", "*");

            Iterator<IStat> statIter = stats.iterator();
            while (statIter.hasNext())
            {
                IStat stat = statIter.next();
                stat.setValue(0);
            }
        }
    }


    /**
     * Reset all current perLife stats but keep the total maximum achieved values
     */
    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event)
    {
        final Player player = event.getEntity();

        //Reset all current stats for this life
        EntityStatBlob statBlob = plugin.getPlayer(player.getName());
        StatVector stats = statBlob.getStats(ExtraStatsPlugin.ourDomain, "*", current + "[a-zA-Z0-9_]*", "*");

        Iterator<IStat> statIter = stats.iterator();
        while (statIter.hasNext())
        {
            IStat stat = statIter.next();
            stat.setValue(0);
        }
    }
}
