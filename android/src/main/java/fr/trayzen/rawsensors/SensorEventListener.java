package fr.trayzen.rawsensors;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Build;

import io.flutter.plugin.common.EventChannel;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class SensorEventListener implements android.hardware.SensorEventListener {

    private EventChannel.EventSink eventCallback;

    public SensorEventListener(EventChannel.EventSink eventCallback) {
        this.eventCallback = eventCallback;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Convert values from float to double in order to return values of same type as the IOS host
        double[] values = new double[event.values.length];

        for (int i = 0; i < event.values.length; i++) {
            values[i] = event.values[i];
        }

        eventCallback.success(values);
    }
}
