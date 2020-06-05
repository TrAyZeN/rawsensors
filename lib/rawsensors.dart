import 'dart:async';

import 'package:flutter/services.dart';

enum SensorType {
  accelerometer,
  temperature,
  gyroscope,
}

class RawSensors {
  static final MethodChannel _setupChannel =
    const MethodChannel('fr.trayzen.rawsensors/setup');

  static final Map<SensorType, EventChannel> _sensorChannels =
    Map.fromIterable(SensorType.values, key: (v) => v,
        value: (v) => EventChannel('fr.trayzen.rawsensors/${typeToName(v)}'));

  static Map<SensorType, Stream<List<double>>> _sensorStreams =
    Map.fromIterable(SensorType.values, key: (v) => v, value: (v) => null);

  static Map<SensorType, int> _sensorAccuracies =
    Map.fromIterable(SensorType.values, key: (v) => v, value: (v) => -1);

  static Future<Stream<List<double>>> getStream(SensorType type, [int accuracy = 2]) async {
    if (_sensorAccuracies[type] != accuracy) {
      final bool result = await _setAccuracy(type, accuracy);

      if (result) {
        _sensorStreams[type] = _sensorChannels[type]
            .receiveBroadcastStream()
            .map((dynamic event) => event.cast<double>());
      } else {
        _sensorStreams[type] = null;
      }
    }

    return _sensorStreams[type];
  }

  static Future<bool> _setAccuracy(SensorType type, int accuracy) async {
    try {
      await _setupChannel.invokeMethod('setAccuracy', <String, dynamic>{
        'sensor': typeToName(type),
        'accuracy': accuracy.toString(),
      });
    } on PlatformException {
      return false;
    }

    _sensorAccuracies[type] = accuracy;
    return true;
  }

  static String typeToName(SensorType type) {
    return type.toString().split('.').last;
  }
}
