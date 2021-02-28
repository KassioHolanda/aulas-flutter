import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';

class PlaceTile extends StatelessWidget {
  final DocumentSnapshot documentSnapshot;

  PlaceTile(this.documentSnapshot);

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: EdgeInsets.symmetric(horizontal: 8.0, vertical: 4.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: <Widget>[
          SizedBox(
            height: 100.0,
            child: Image.network(
              documentSnapshot.data['image'],
              fit: BoxFit.cover,
            ),
          ),
          Container(
            padding: EdgeInsets.all(8.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Text(
                  documentSnapshot.data['title'],
                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 17.0),
                  textAlign: TextAlign.start,
                ),
                Text(
                  documentSnapshot.data['title'],
                  style: TextStyle(fontSize: 14.0),
                  textAlign: TextAlign.start,
                ),
              ],
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.end,
            children: <Widget>[
              FlatButton(
                onPressed: () {
                  launch(
                      'https://www.google.com./maps/search/?api=1&query=${documentSnapshot
                          .data['lat']},${documentSnapshot.data['long']}');
                },
                padding: EdgeInsets.zero,
                child: Text('Ver no Mapa'),
              ),
              FlatButton(
                onPressed: () {
                  launch('tel:${documentSnapshot.data['phone']}');
                },
                padding: EdgeInsets.zero,
                child: Text('Ligar'),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
