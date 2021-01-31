package singa.tech.fresh4kitchen.util;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/zcool_regular.ttf");
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectionDetector.ConnectivityReceiverListener listener) {
        ConnectionDetector.connectivityReceiverListener = listener;
    }
}
