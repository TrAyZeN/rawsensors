package fr.trayzen.rawsensors;

import android.content.Context;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** RawsensorsPlugin */
public class RawsensorsPlugin implements FlutterPlugin {

    private static final String SETUP_CHANNEL = "fr.trayzen.rawsensors/setup";
    private MethodChannel setupChannel;

    private SensorEventListenerManager sensorEventListenerManager;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        setup(flutterPluginBinding.getApplicationContext(), flutterPluginBinding.getBinaryMessenger());
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        RawsensorsPlugin plugin = new RawsensorsPlugin();
        plugin.setup(registrar.context(), registrar.messenger());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        tearDown();
  }

    public void setup(Context context, BinaryMessenger messenger) {
        setupChannel = new MethodChannel(messenger, SETUP_CHANNEL);
        setupChannel.setMethodCallHandler(new SetupChannelHandler(this));

        final SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorEventListenerManager = new SensorEventListenerManager(sensorManager, messenger);
    }

    private void tearDown() {
        setupChannel.setMethodCallHandler(null);
        sensorEventListenerManager.removeListeners();
    }

    public SensorEventListenerManager getSensorEventListenerManager() {
        return sensorEventListenerManager;
    }
}
