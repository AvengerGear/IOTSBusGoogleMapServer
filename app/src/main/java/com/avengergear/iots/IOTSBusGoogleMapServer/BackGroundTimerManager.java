package com.avengergear.iots.IOTSBusGoogleMapServer;

import java.util.HashMap;
import java.util.Timer;

public class BackGroundTimerManager {

    public static HashMap<String, Timer> timers = new HashMap<String, Timer>();

    public static Timer getTimer(String name) {
        if( !timers.containsKey(name) ) {
            timers.put(name, new Timer(name));
        }
        return timers.get(name);
    }

    public static boolean removeTimer(String name) {
        if( timers.containsKey(name) ) {
            timers.remove(name);
            return true;
        }
        return false;
    }

    public static HashMap<String, Timer> getTimers() {
        return timers;
    }

    public static void clearTimers() {
        timers.clear();
    }
}
