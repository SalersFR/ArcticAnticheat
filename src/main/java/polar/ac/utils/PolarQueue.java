package polar.ac.utils;

import lombok.Getter;
import polar.ac.Polar;

import java.util.Collection;
import java.util.LinkedList;

public class PolarQueue<T> extends LinkedList<T> {

    @Getter
    private final int maxSize;

    public PolarQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public PolarQueue(Collection<? extends T> c, int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T t) {
        if (size() >= getMaxSize()) clear();
        return super.add(t);
    }
}
