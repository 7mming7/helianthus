package com.ha.event;

import com.google.common.base.Preconditions;

/**
 * User: shuiqing
 * DateTime: 17/7/11 下午5:22
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class Event {
    public enum Type {
        FLOW_STARTED,
        FLOW_FINISHED,
        JOB_STARTED,
        JOB_FINISHED,
        JOB_STATUS_CHANGED,
        EXTERNAL_FLOW_UPDATED,
        EXTERNAL_JOB_UPDATED
    }

    private final Object runner;
    private final Type type;
    private final EventData eventData;
    private final long time;

    private Event(Object runner, Type type, EventData eventData) {
        this.runner = runner;
        this.type = type;
        this.eventData = eventData;
        this.time = System.currentTimeMillis();
    }

    public Object getRunner() {
        return runner;
    }

    public Type getType() {
        return type;
    }

    public long getTime() {
        return time;
    }

    public EventData getData() {
        return eventData;
    }

    /**
     * Creates a new event.
     *
     * @param runner runner.
     * @param type type.
     * @param eventData EventData, null is not allowed.
     * @return New Event instance.
     * @throws NullPointerException if EventData is null.
     */
    public static Event create(Object runner, Type type, EventData eventData) throws NullPointerException {
        Preconditions.checkNotNull(eventData, "EventData was null");
        return new Event(runner, type, eventData);
    }

}