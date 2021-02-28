import 'package:flare_flutter/flare_actor.dart';
import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: SplashScreen(),
    );
  }
}

class SplashScreen extends StatefulWidget {
  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Container(
          width: 100,
          height: 100,
          child: FlareActor(
            'assets/animations/gears.flr',
            animation: 'Spin1',
          ),
        ),
      ),
    );
  }

  @override
  void initState() {
    super.initState();
//    Future.delayed(Duration(seconds: 3)).then((of) {
//      Navigator.of(context)
//          .pushReplacement(MaterialPageRoute(builder: (cont) => HomeScreen()));
//    });
  }
}

class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Text(
            'Hello Flare',
            textAlign: TextAlign.center,
            style: TextStyle(fontSize: 30),
          ),
        ],
      ),
    );
  }
}
