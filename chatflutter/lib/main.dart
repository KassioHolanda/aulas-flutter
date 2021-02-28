import 'package:chatflutter/chat_screen.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';

void main() async {
  runApp(MyApp());
//  Firestore.instance.collection("mensagens").snapshots().listen((data) {
////    print(data.documents[0].data);
//    data.documents.forEach((d) {
//      print(d.data);
//    });
//  });
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        iconTheme: IconThemeData(
          color: Colors.blue,
        ),
        primarySwatch: Colors.blue,
      ),
      home: ChatScreen(),
    );
  }
}
