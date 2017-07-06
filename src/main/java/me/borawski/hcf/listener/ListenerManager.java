package me.borawski.hcf.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import me.borawski.hcf.Core;

/**
 * Created by Ethan on 3/8/2017.
 */
public class ListenerManager {

    private List<Listener> listeners;

    public ListenerManager() {
        listeners = new ArrayList<Listener>();
    }

    public List<Listener> getListenerList() {
        return listeners;
    }

    public void addListener(Listener l) {
        listeners.add(l);
        Bukkit.getPluginManager().registerEvents(l, Core.getInstance());
    }
    
}
