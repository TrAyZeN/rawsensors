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
                plugin.getSensorEventListenerManager().setListener(
                    (String) call.argument("sensor"),
                    Integer.parseInt((String) call.argument("accuracy"))
                );
            } catch (Exception e) {
                result.error("", e.getMessage(), e.getCause());
                return;
            }
            result.success(null);
        } else if (call.method.equals("isAvailable")) {
            result.success(Boolean.toString(
                plugin.getSensorEventListenerManager().isSensorAvailable((String) call.argument("sensor"))));
        } else {
            result.notImplemented();
        }
    }
}
