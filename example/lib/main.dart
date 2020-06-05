import 'package:flutter/material.dart';
import 'package:rawsensors_example/sensor.dart';
import 'package:rawsensors/rawsensors.dart';

void main() => runApp(MaterialApp(
  title: 'rawsensors example',
  home: HomeScreen(),
));

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('rawsensors example'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: SensorType.values.map(
              (t) => RaisedButton(
                child: Text(RawSensors.typeToName(t)),
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) => SensorScreen(t))
                  );
                },
              )
            ).toList(),
          ),
        ),
      ),
    );
  }
}
