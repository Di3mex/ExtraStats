package com.extrahardmode.beardstat.perlifestats;


import com.extrahardmode.ExtraHardMode;
import com.tehbeard.BeardStat.BeardStat;
import com.tehbeard.BeardStat.containers.EntityStatBlob;
import com.tehbeard.BeardStat.containers.IStat;
import com.tehbeard.BeardStat.containers.PlayerStatManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Diemex
 */
public class ExtraStatsPlugin extends JavaPlugin implements Listener
{
    public final static String ourDomain = "extrastats";
    private PlayerStatManager playerStatManager;
    protected ExtraHardMode awesomeEhm;


    @Override
    public void onEnable()
    {
        playerStatManager = getBeardstat().getStatManager();
        awesomeEhm = (ExtraHardMode) getServer().getPluginManager().getPlugin("ExtraHardMode");
        getServer().getPluginManager().registerEvents(this, this);
        new BowHitsListener(this);
        new SheepShearListener(this);
        new TntKillsListener(this);
        new EndermanDayListener(this);
        new PerLifeListener(this);
        new RareDropListener(this);
    }


    public IStat getStat(String player, String world, String category, String statId)
    {
        //creates a new stat defaulting to 0 if not found
        return getPlayer(player).getStat(ourDomain, world, category, statId);
    }


    /**
     * Increment a stat that also has a total/maximum stat*
     *
     * @param player   player of the stat
     * @param world    world player is in
     * @param cat      category e.g. "totalkills"
     * @param totalCat category of the stat that holds the greatest value the stat has ever reached
     * @param stat     stat e.g. "enderman"
     */
    public void incrWithTotal(String player, String world, String cat, String totalCat, String stat)
    {
        IStat currStat = getStat(player, world, cat, stat);
        currStat.incrementStat(1);
        //Update total/max value
        updateIfGreater(player, world, totalCat, stat, currStat.getValue());
    }


    /**
     * Update a stat if the new value is greater
     *
     * @param player player of the stat
     * @param world  world player is in
     * @param cat    category e.g. "totalkills"
     * @param stat   stat e.g. "enderman"
     * @param newVal newValue to test against the currentValue
     */
    public void updateIfGreater(String player, String world, String cat, String stat, int newVal)
    {
        //Update total/max value
        IStat totalStat = getStat(player, world, cat, stat);
        if (newVal > totalStat.getValue())
            totalStat.setValue(newVal);
    }


    public EntityStatBlob getPlayer(String player)
    {
        return playerStatManager.getPlayerBlob(player);
    }


    public ExtraHardMode getAwesomeEhm()
    {
        return awesomeEhm;
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
