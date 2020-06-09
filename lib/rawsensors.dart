import 'dart:async';

import 'package:flutter/services.dart';

enum SensorType {
  accelerometer,
  temperature,
  gyroscope,
  magnetometer,
  light,
}

class RawSensors {
  factory RawSensors() {
    if (_singleton == null) {
      MethodChannel setupChannel =
          const MethodChannel('fr.trayzen.rawsensors/setup');

      Map<SensorType, EventChannel> sensorChannels = Map.fromIterable(
          SensorType.values,
          key: (v) => v,
          value: (v) => EventChannel('fr.trayzen.rawsensors/${typeToName(v)}'));

      Map<SensorType, Stream<List<double>>> sensorStreams =
          Map.fromIterable(SensorType.values, key: (v) => v, value: (v) => null);

      Map<SensorType, int> sensorAccuracies =
          Map.fromIterable(SensorType.values, key: (v) => v, value: (v) => -1);

      _singleton = new RawSensors._constructor(
        setupChannel,
        sensorChannels,
        sensorStreams,
        sensorAccuracies
      );
    }

    return _singleton;
  }

  RawSensors._constructor(
    this._setupChannel,
    this._sensorChannels,
    this._sensorStreams,
    this._sensorAccuracies
  );

  static RawSensors _singleton;
  final MethodChannel _setupChannel;
  final Map<SensorType, EventChannel> _sensorChannels;
  Map<SensorType, Stream<List<double>>> _sensorStreams;
  Map<SensorType, int> _sensorAccuracies;

  /// Returns a [Future] containing the [Stream] of raw data from the sensor of
  /// the corresponding sensor type
  Future<Stream<List<double>>> getStream(SensorType type,
      [int accuracy = 60000]) async {
    if (accuracy < 0) {
      throw new ArgumentError('Accuracy must be an absolute value');
    }

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

  Future<bool> _setAccuracy(SensorType type, int accuracy) async {
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
