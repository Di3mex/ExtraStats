package com.extrahardmode.extrastats;


import com.tehbeard.BeardStat.containers.IStat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

/**
 * @author Diemex
 */
public class SheepShearListener implements Listener
{
    private final ExtraStatsPlugin plugin;


    public SheepShearListener(ExtraStatsPlugin plugin)
    {
        this.plugin = plugin;
    }


    /**
     * Shear statistic that also contains the color of the sheared sheep
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST) //EHM changes all the sheep to white sheep
    private void onSheepShear(PlayerShearEntityEvent event)
    {
        if (event.getEntity() instanceof Sheep)
        {
            final Player player = event.getPlayer();
            Sheep sheep = (Sheep) event.getEntity();
            IStat shearStat = plugin.getStat(player.getName(), player.getWorld().getName(), "sheepsheared", sheep.getColor().name().toLowerCase().replace("_", ""));
            shearStat.incrementStat(1);
        }
    }
}
