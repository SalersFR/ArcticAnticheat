package dev.arctic.anticheat.threading;

import dev.arctic.anticheat.data.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ThreadingManager {

    private final List<ArcticThread> threads = new ArrayList<>();

    public void addPlayer(final PlayerData data) {
        final Optional<ArcticThread> threadOptional = threads.stream().filter(thread ->!thread.isFull()).findFirst();

        if(threadOptional.isPresent()) {
            threadOptional.get().add(data);
        } else {
            addNewThread();
        }

    }

    public void remove(final PlayerData data) {
        for(ArcticThread arcticThreads : threads) {
            if(arcticThreads.contains(data))
                arcticThreads.remove(data);
        }
    }



    public ThreadingManager() {
        threads.add(new ArcticThread());
    }



    public void addNewThread() {
        this.threads.add(new ArcticThread());
    }
}
