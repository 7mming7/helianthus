package com.ha.event;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: shuiqing
 * DateTime: 17/7/11 下午5:25
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class EventHandler {
    private HashSet<EventListener> listeners = new HashSet<EventListener>();

    public EventHandler() {
    }

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void fireEventListeners(Event event) {
        ArrayList<EventListener> listeners =
                new ArrayList<EventListener>(this.listeners);
        for (EventListener listener : listeners) {
            listener.handleEvent(event);
        }
    }

    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }
}
