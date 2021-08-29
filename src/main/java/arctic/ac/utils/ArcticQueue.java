package arctic.ac.utils;

import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

public class ArcticQueue<T> extends LinkedList<T> {

    @Getter
    private final int maxSize;

    public ArcticQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public ArcticQueue(Collection<? extends T> c, int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T t) {
        if (size() >= getMaxSize()) clear();
        return super.add(t);
    }
}
