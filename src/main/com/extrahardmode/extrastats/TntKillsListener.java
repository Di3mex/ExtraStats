package com.extrahardmode.extrastats;


import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Diemex
 */
public class TntKillsListener implements Listener
{
    private final ExtraStatsPlugin plugin;


    public TntKillsListener(ExtraStatsPlugin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    private final Map<UUID/*Tnt Id*/, Map<String/*EntityId*/, Integer/*KillCount*/>> tntKills = new HashMap<UUID, Map<String, Integer>>();


    @EventHandler
    private void onExplode(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Monster)
        {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (event.getDamager() instanceof TNTPrimed && entity.getHealth() - event.getDamage() <= 0) //Will die
            {
                TNTPrimed tnt = (TNTPrimed) event.getDamager();
                if (tnt.getSource() instanceof Player)
                {
                    if (!tntKills.containsKey(tnt.getUniqueId()))
                    {
                        tntKills.put(tnt.getUniqueId(), new HashMap<String, Integer>());
                        plugin.getServer().getScheduler().runTaskLater(plugin,
                                new Evaluate(tnt.getUniqueId(), ((Player) tnt.getSource()).getName(), tnt.getWorld().getName()), 1);
                    }
                    Map<String, Integer> kills = tntKills.get(tnt.getUniqueId());
                    String type = entity.getType().getName().toLowerCase().replace("_", "");

                    //Extra Type for charged creepers
                    if (entity instanceof Creeper && ((Creeper) entity).isPowered())
                        type = "charged" + type;

                    int killsVal = kills.containsKey(type) ? kills.get(type) : 0;
                    kills.put(type, ++killsVal);
                }
            }
        }
    }


    private class Evaluate implements Runnable
    {
        private final UUID primed;
        private final String name;
        private final String world;


        protected Evaluate(UUID primed, String player, String world)
        {
            this.primed = primed;
            this.name = player;
            this.world = world;
        }


        @Override
        public void run()
        {
            Map<String, Integer> kills = tntKills.remove(primed);
            //Stat for every entity
            int total = 0;
            for (Map.Entry<String, Integer> entry : kills.entrySet())
            {
                plugin.getStat(name, world, "totaltntkills", entry.getKey()).incrementStat(entry.getValue());
                plugin.updateIfGreater(name, world, "maxtntkills", entry.getKey(), entry.getValue());
                total += entry.getValue();
            }
            //Total tnt kills
            plugin.getStat(name, world, "totaltntkills", "total").incrementStat(total);
            plugin.updateIfGreater(name, world, "maxtntkills", "total", total);
        }
    }
}
