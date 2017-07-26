package com.ha.utils;

/**
 * User: shuiqing
 * DateTime: 17/7/18 下午3:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Queue that swaps its lists. Allows for non-blocking writes when reading. Swap
 * should be called before every read.
 */
public class SwapQueue<T> implements Iterable<T> {
    ArrayList<T> primaryQueue;
    ArrayList<T> secondaryQueue;

    public SwapQueue() {
        primaryQueue = new ArrayList<T>();
        secondaryQueue = new ArrayList<T>();
    }

    /**
     * Swaps primaryQueue with secondary queue. The previous primary queue will be
     * released.
     */
    public synchronized void swap() {
        primaryQueue = secondaryQueue;
        secondaryQueue = new ArrayList<T>();
    }

    /**
     * Returns a count of the secondary queue.
     *
     * @return
     */
    public synchronized int getSwapQueueSize() {
        return secondaryQueue.size();
    }

    public synchronized int getPrimarySize() {
        return primaryQueue.size();
    }

    public synchronized void addAll(Collection<T> col) {
        secondaryQueue.addAll(col);
    }

    /**
     * Returns both the secondary and primary size
     *
     * @return
     */
    public synchronized int getSize() {
        return secondaryQueue.size() + primaryQueue.size();
    }

    public synchronized void add(T element) {
        secondaryQueue.add(element);
    }

    /**
     * Returns iterator over the primary queue.
     */
    @Override
    public synchronized Iterator<T> iterator() {
        return primaryQueue.iterator();
    }
}
