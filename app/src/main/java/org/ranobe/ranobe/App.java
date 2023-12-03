package org.ranobe.ranobe;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.material.color.DynamicColors;

import org.ranobe.ranobe.config.Ranobe;
import org.ranobe.ranobe.ui.error.CrashActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class App extends Application implements LifecycleObserver {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return App.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Ranobe.DEBUG, "app launched");
        DynamicColors.applyToActivitiesIfAvailable(this);
        App.context = getApplicationContext();

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        Thread.setDefaultUncaughtExceptionHandler(
                (thread, e) -> {


                    Intent intent = new Intent(getApplicationContext(), CrashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String stackTrace = sw.toString(); // stack trace as a string
                    intent.putExtra("ex", stackTrace);
                    startActivity(intent);
                    System.exit(1);
                });

    }
}
