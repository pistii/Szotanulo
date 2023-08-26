package com.pisti.szotanulo;

import android.util.Log;

public class CallbackEventListener implements EventListener {

    @Override
    public void onTrigger() {
        Log.d("Callback", "running callback in implemented class");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
