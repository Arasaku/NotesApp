package com.example.main.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors instance;

    private final Executor diskIO;
    private final Executor mainThread;

    private AppExecutors() {
        // Пул из 4 потоков для операций с БД
        diskIO = Executors.newFixedThreadPool(4);
        mainThread = new MainThreadExecutor();
    }

    public static synchronized AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    public Executor diskIO() { return diskIO; }
    public Executor mainThread() { return mainThread; }

    private static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }
    }
}
