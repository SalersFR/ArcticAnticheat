package dev.arctic.anticheat.utilities;

import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

public final class EvictingList<T> extends LinkedList<T> {
    @Getter
    private final int maxSize;
    @Getter
    private boolean clearOnMax;

    public EvictingList(int maxSize, boolean clearOnMax) {
        this.maxSize = maxSize;
        this.clearOnMax = clearOnMax;
    }

    public EvictingList(Collection<? extends T> c, int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T t) {

        if(size() >= getMaxSize()) {
            if (!clearOnMax) {
                removeFirst();
            } else if (clearOnMax) {
               clear();
            }
        }
        return super.add(t);
    }
}