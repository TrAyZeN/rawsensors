package fr.trayzen.rawsensors;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class SetupChannelHandler implements MethodChannel.MethodCallHandler {

    private RawsensorsPlugin plugin;

    public SetupChannelHandler(RawsensorsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("setAccuracy")) {
            try {
                final String sensor = call.argument("sensor");
                final String accuracy = call.argument("accuracy");
                plugin.getSensorEventListenerManager().setListener(sensor, Integer.parseInt(accuracy));
            } catch (Exception e) {
                result.error("", e.getMessage(), e.getCause());
                return;
            }

            result.success(null);
        } else {
            result.notImplemented();
        }
    }
}
