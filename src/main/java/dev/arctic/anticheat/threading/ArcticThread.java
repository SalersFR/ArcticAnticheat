package dev.arctic.anticheat.threading;

import dev.arctic.anticheat.data.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArcticThread {

    private final ExecutorService executorService;
    private final List<PlayerData> playersInThread;

    public ArcticThread() {
        this.executorService = Executors.newSingleThreadExecutor();
        this.playersInThread = new ArrayList<>(25);
    }

    public boolean isFull() {
        return playersInThread.size() == 25;
    }

    public void add(final PlayerData data) {
        this.playersInThread.add(data);
        data.setThread(this);
    }

    public void remove(final PlayerData data) {
        this.playersInThread.remove(data);
    }

    public boolean contains(final PlayerData data) {
        return this.playersInThread.contains(data);
    }

    public void run(final Runnable runnable) {
        this.executorService.execute(runnable);
    }
}
