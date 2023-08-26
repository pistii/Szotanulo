package com.pisti.szotanulo;

import android.util.Log;

public class EventConsumer {
    public final EventListener eventListener;

    public EventConsumer(EventListener eventListener) {
        this.eventListener = eventListener;
    }

}
