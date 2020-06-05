import 'dart:async';

import 'package:flutter/material.dart';
import 'package:rawsensors/rawsensors.dart';

class SensorScreen extends StatefulWidget {
  final SensorType sensorType;

  SensorScreen(this.sensorType);

  @override
  _SensorScreenState createState() => _SensorScreenState();
}

class _SensorScreenState extends State<SensorScreen> with WidgetsBindingObserver {
  StreamSubscription<List<double>> _streamSubscription;
  List<double> _data;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(RawSensors.typeToName(widget.sensorType)),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: _data?.asMap()?.entries?.map(
            (entry) => Text('data[${entry.key}] = ${entry.value.toStringAsFixed(10)}')
          )?.toList()
            ?? <Widget> [Text('Unknown')],
        ),
      ),
    );
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      subscribeToSensorDataStream();
    } else if (_streamSubscription != null) {
      _streamSubscription.cancel();
    }
  }

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);

    subscribeToSensorDataStream();
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);
    super.dispose();

    if (_streamSubscription != null)
      _streamSubscription.cancel();
  }

  void subscribeToSensorDataStream() {
    RawSensors.getStream(widget.sensorType)
      .then((Stream sensorStream) {
        _streamSubscription = sensorStream.listen((dynamic data) {
          setState(() {
            _data = data;
          });
        });
      });
  }
}
