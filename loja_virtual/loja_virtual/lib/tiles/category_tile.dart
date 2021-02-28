import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:loja_virtual/screens/products_screen.dart';

class CategoryTile extends StatelessWidget {
  final DocumentSnapshot snapshot;

  CategoryTile(this.snapshot);

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      margin: EdgeInsets.all(4.0),
      padding: EdgeInsets.all(4.0),
      child: ListTile(
//      contentPadding: EdgeInsets.all(8.0),
        leading: CircleAvatar(
          radius: 35.0,
          backgroundColor: Colors.transparent,
          backgroundImage: NetworkImage(snapshot.data['icon']),
        ),
        title: Text(
          snapshot.data['title'],
//          style: TextStyle(color: Colors.white),
        ),
        trailing: Icon(
          Icons.keyboard_arrow_right,
//          color: Colors.white,
        ),
        onTap: () {
          Navigator.of(context).push(MaterialPageRoute(builder: (context) {
            return ProductsScreen(snapshot);
          }));
        },
      ),
    );
  }
}
