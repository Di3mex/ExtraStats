package com.extrahardmode.beardstat.perlifestats;


import com.tehbeard.BeardStat.BeardStat;
import com.tehbeard.BeardStat.containers.EntityStatBlob;
import com.tehbeard.BeardStat.containers.IStat;
import com.tehbeard.BeardStat.containers.PlayerStatManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Diemex
 */
public class ExtraStatsPlugin extends JavaPlugin implements Listener
{
    private final String ourDomain = "extrastats";
    private PlayerStatManager playerStatManager;


    @Override
    public void onEnable()
    {
        playerStatManager = getBeardstat().getStatManager();
        getServer().getPluginManager().registerEvents(this, this);
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
                    IStat currStat = getStat(player, player.getWorld().getName(), "bowhitdist", entity instanceof Player ? "player" : "monster");
                    //Update value if new shot is further away
                    if (distance > currStat.getValue())
                        currStat.setValue(distance);

                    //Separate stat for bowkills
                    if (entity.getHealth() - event.getDamage() <= 0)
                    {
                        IStat killStat = getStat(player, player.getWorld().getName(), "bowkilldist", entity instanceof Player ? "player" : "monster");
                        if (distance > killStat.getValue())
                            killStat.setValue(distance);
                    }
                }
            }
        }
    }


    public IStat getStat(Player player, String world, String category, String statId)
    {
        //creates a new stat defaulting to 0 if not found
        return getPlayer(player.getName()).getStat(ourDomain, world, category, statId);
    }


    private EntityStatBlob getPlayer(String player)
    {
        return playerStatManager.getPlayerBlob(player);
    }


    /**
     * Retrieve a running instance of BeardStat
     */
    private BeardStat getBeardstat()
    {
        //We depend on BeardStat so we don't have to check anything
        return (BeardStat) getServer().getPluginManager().getPlugin("BeardStat");
    }
}
