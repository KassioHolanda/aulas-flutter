import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:loja_virtual/models/cart_model.dart';
import 'package:loja_virtual/models/user_model.dart';
import 'package:loja_virtual/screens/home_screen.dart';
import 'package:scoped_model/scoped_model.dart';

void main() {
  runApp(MyApp());
//  Firestore.instance.collection('messages').add({
//    'teste': 'teste',
//  });
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return ScopedModel<UserModel>(
        model: UserModel(),
        child: ScopedModelDescendant<UserModel>(
//          model: CartModel(),
          builder: (context, child, model) {
            return ScopedModel<CartModel>(
              model: CartModel(model),
              child: MaterialApp(
                title: 'Flutter´s Chothing',
                debugShowCheckedModeBanner: false,
                theme: ThemeData(
                  primaryColor: Color.fromARGB(255, 4, 125, 141),
                  primarySwatch: Colors.blue,
                ),
                home: HomeScreen(),
              ),
            );
          },
        ));
  }
}
