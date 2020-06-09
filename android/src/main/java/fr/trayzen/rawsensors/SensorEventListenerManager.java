package fr.trayzen.rawsensors;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.Hashtable;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;

public class SensorEventListenerManager {

    private SensorManager sensorManager;

    private Hashtable<String, Integer> sensorTypes;
    private Hashtable<String, EventChannel> sensorChannels;

    public SensorEventListenerManager(SensorManager sensorManager, BinaryMessenger messenger) {
        this.sensorManager = sensorManager;

        sensorTypes = new Hashtable<String, Integer>();
        registerSensorTypes();

        sensorChannels = new Hashtable<String, EventChannel>();
        setupChannels(messenger);
    }

    private void registerSensorTypes() {
        sensorTypes.put("accelerometer", Sensor.TYPE_ACCELEROMETER);
        sensorTypes.put("temperature", Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorTypes.put("gyroscope", Sensor.TYPE_GYROSCOPE);
        sensorTypes.put("magnetometer", Sensor.TYPE_MAGNETIC_FIELD);
        sensorTypes.put("light", Sensor.TYPE_LIGHT);
    }

    private void setupChannels(BinaryMessenger messenger) {
        for (String sensor : sensorTypes.keySet()) {
            sensorChannels.put(sensor, new EventChannel(messenger, "fr.trayzen.rawsensors/" + sensor));
        }
    }

    public Boolean isSensorAvailable(String sensor) {
        return sensorManager.getDefaultSensor(sensorTypes.get(sensor)) != null;
    }

    public void setListener(String sensor, int accuracy) {
        sensorChannels.get(sensor).setStreamHandler(
                new SensorStreamHandler(sensorManager, sensorTypes.get(sensor), accuracy));
    }

    public void removeListeners() {
        for (EventChannel channel : sensorChannels.values()) {
            channel.setStreamHandler(null);
        }
    }
}
