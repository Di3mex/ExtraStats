package com.extrahardmode.extrastats;


import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * @author Diemex
 */
public class EndermanDayListener implements Listener
{
    private final ExtraStatsPlugin plugin;


    public EndermanDayListener(ExtraStatsPlugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    /**
     * When an Enderman is killed in daylight
     */
    @EventHandler
    public void onEndermanDeath(EntityDeathEvent event)
    {
        if (event.getEntity() instanceof Enderman)
        {
            Enderman enderman = (Enderman) event.getEntity();
            if (enderman.getLocation().getBlock().getLightLevel() >= 15 && enderman.getKiller() != null)
            {
                Player player = (Player) enderman.getKiller();
                plugin.getStat(player.getName(), player.getWorld().getName(), "daylightkills", event.getEntityType().name().toLowerCase().replace("_", "")).incrementStat(1);
            }
        }
    }
}
