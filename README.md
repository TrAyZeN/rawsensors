<h1 align="center">
    rawsensors
</h1>

> A flutter plugin that lets you access raw data from sensors
The goal of this plugin is to expose every sensors from the Android and IOS framework.

## Features
Different sensor types are exposed via the enum `SensorType`:
Sensor types available:
- Accelerometer
- Temperature
- Gyroscope
- Magnetometer
- Light

**Warning**: This plugin does not support IOS for now, I would greatly appreciate some help to support IOS.

## Usage
To use this plugin, add `rawsensors` as a [dependency in your pubspec.yaml file](https://flutter.dev/docs/development/packages-and-plugins/using-packages). For example:
```yaml
dependencies:
  rawsensors: 0.0.1
```

## Example
```dart
import 'package:rawsensors/rawsensors.dart';

RawSensors.getStream(SensorType.accelerometer)
  .then((Stream sensorStream) => {
    if (sensorStream != null) {
      sensorStream.listen((dynamic data) {
        print(data);
      }
    } else {
      print("No accelerometer found on device");
    }
  }));
```

## Issues
If you have any issue, bug or feature request please [open an issue](https://github.com/TrAyZeN/rawsensors/issues/new).

## Contribution
Contributions are welcomed, feel free to contribute if you see any improvement or you want to add new features.

## License
MIT TrAyZeN
