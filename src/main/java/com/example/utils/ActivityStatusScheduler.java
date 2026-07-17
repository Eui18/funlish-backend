package com.example.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.services.ActivityService;

public class ActivityStatusScheduler {

    private final ActivityService activityService;
    private final ScheduledExecutorService executor;

    public ActivityStatusScheduler(ActivityService activityService) {
        this.activityService = activityService;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {

        executor.scheduleAtFixedRate(
                this::runSafely,
                0,
                1,
                TimeUnit.MINUTES
        );
    }

    public void stop() {
        executor.shutdown();
    }

    private void runSafely() {

        try {
            activityService.finishExpiredActivities();
        } catch (Exception e) {
            System.out.println("Error al finalizar actividades vencidas: " + e.getMessage());
        }
    }
}