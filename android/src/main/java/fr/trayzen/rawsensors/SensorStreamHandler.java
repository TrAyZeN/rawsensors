package fr.trayzen.rawsensors;

import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

import io.flutter.plugin.common.EventChannel;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class SensorStreamHandler implements EventChannel.StreamHandler {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private int accuracy;

    public SensorStreamHandler(SensorManager sensorManager, int sensorType, int accuracy) {
        this.sensorManager = sensorManager;
        sensor = sensorManager.getDefaultSensor(sensorType);
        this.accuracy = accuracy;
    }

    @Override
    public void onListen(Object o, EventChannel.EventSink events) {
        sensorEventListener = new SensorEventListener(events);
        sensorManager.registerListener(sensorEventListener, sensor, accuracy);
    }

    @Override
    public void onCancel(Object o) {
        sensorManager.unregisterListener(sensorEventListener);
    }
}
