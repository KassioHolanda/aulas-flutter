import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:loja_virtual/tiles/category_tile.dart';

class CategoryTab extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return FutureBuilder<QuerySnapshot>(
      future: Firestore.instance.collection('products').getDocuments(),
      builder: (context, snapshot) {
        if (!snapshot.hasData) {
          return Center(
            child: CircularProgressIndicator(),
          );
        } else {
//          var dividerTiles = ListTile.divideTiles(
//              tiles: snapshot.data.documents.map((document) {
//                return CategoryTile(document);
//              }).toList(),
//              color: Colors.white).toList();
//
//          return ListView(children: dividerTiles);
          return ListView(
            children: snapshot.data.documents.map((document) {
              return CategoryTile(document);
            }).toList(),
          );
        }
      },
    );
  }
}
