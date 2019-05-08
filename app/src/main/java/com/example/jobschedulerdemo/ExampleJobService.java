package com.example.jobschedulerdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExampleJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");

        doBackgroundWork(params);

        //returning true keeps the CPU running until the job finishes using a wakelock
        //however we are responsible to tell the system when we are actually finished to release the wake lock
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    if (jobCancelled) //this boolean will notify us if our job is stopped, so we must cancel the background thread
                        return;
                    Log.d(TAG, "run: " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, "Job Finished");

                //second boolean is if you want to reschedule based on setBackOffCritieria()
                //but since we have no backing off critieria and we are periodically retrying after a certain amount of time
                //we pass false
                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");

        jobCancelled = true; //on stop job maybe called if wifi was turned off given that it was required.
        return true; //returning true means we retry the service if it fails
    }
}
