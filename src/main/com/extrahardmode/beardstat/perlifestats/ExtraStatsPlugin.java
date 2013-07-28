package com.extrahardmode.beardstat.perlifestats;


import com.tehbeard.BeardStat.BeardStat;
import com.tehbeard.BeardStat.containers.EntityStatBlob;
import com.tehbeard.BeardStat.containers.IStat;
import com.tehbeard.BeardStat.containers.PlayerStatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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