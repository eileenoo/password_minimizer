package application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class PicturePasswordApplication extends Application
{
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        PicturePasswordApplication.context = getApplicationContext();
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }

    public static Context getAppContext()
    {
        return PicturePasswordApplication.context;
    }
}
