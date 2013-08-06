package com.extrahardmode.extrastats;


import com.tehbeard.BeardStat.containers.IStat;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author Diemex
 */
public class BowHitsListener implements Listener
{
    private final ExtraStatsPlugin plugin;


    public BowHitsListener(ExtraStatsPlugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    /**
     * Extra statistic for the distance when you hit a monster/player with a bow
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    private void onBowHit(EntityDamageByEntityEvent event)
    {
        LivingEntity entity = event.getEntity() instanceof LivingEntity ? (LivingEntity) event.getEntity() : null;
        //Only mobs, killing animals is too easy
        if (event.getDamager() instanceof Arrow)
        {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player)
            {
                Player player = (Player) arrow.getShooter();
                //Slime is not a monster for some reason...
                if (entity instanceof Monster || entity instanceof Player)
                {
                    //get distance between shooter and damagee
                    int distance = (int) event.getEntity().getLocation().distance(player.getLocation());
                    IStat currStat = plugin.getStat(player.getName(), player.getWorld().getName(), "bowhitdist", entity instanceof Player ? "player" : "monster");
                    //Update value if new shot is further away
                    if (distance > currStat.getValue())
                        currStat.setValue(distance);

                    //Separate stat for bowkills
                    if (entity.getHealth() - event.getDamage() <= 0)
                    {
                        IStat killStat = plugin.getStat(player.getName(), player.getWorld().getName(), "bowkilldist", entity instanceof Player ? "player" : "monster");
                        if (distance > killStat.getValue())
                            killStat.setValue(distance);
                    }
                }
            }
        }
    }
}
