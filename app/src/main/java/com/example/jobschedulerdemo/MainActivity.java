package com.example.jobschedulerdemo;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void stopJob(View view) {
        JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(223);
        Log.d(TAG, "JobCancelled");
    }

    public void scheduleJob(View view) {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(223, componentName)
                .setRequiresCharging(true)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS)
            Log.d(TAG, "jobStarted");
        else
            Log.d(TAG, "jobFailedToStart");

    }
}
