import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:loja_virtual/models/cart_model.dart';

class DiscountCart extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Card(
      margin: EdgeInsets.symmetric(horizontal: 8.0, vertical: 4.0),
      child: ExpansionTile(
        title: Text(
          'Cupom de Desconto',
          textAlign: TextAlign.start,
          style:
              TextStyle(fontWeight: FontWeight.w500, color: Colors.grey[700]),
        ),
        leading: Icon(Icons.card_giftcard),
        trailing: Icon(Icons.add),
        children: <Widget>[
          Padding(
            padding: EdgeInsets.all(8.0),
            child: TextFormField(
              initialValue: CartModel.of(context).cupomCode ?? '',
              onFieldSubmitted: (text) {
                Firestore.instance
                    .collection('coupons')
                    .document(text)
                    .get()
                    .then((data) {
                  if (data.data != null) {
                    Scaffold.of(context).showSnackBar(SnackBar(
                      content: Text(
                          'Desconto de ${data.data['percent']}% aplicado!'),
                      backgroundColor: Colors.blue,
                    ));
                    CartModel.of(context).setCoupon(text, data.data['percent']);
                  } else {
                    Scaffold.of(context).showSnackBar(SnackBar(
                      content: Text(
                          'Cupom não existe!'),
                      backgroundColor: Colors.red,
                    ));
                    CartModel.of(context).setCoupon(null, 0);
                  }
                });
              },
              decoration: InputDecoration(
                  border: OutlineInputBorder(), hintText: 'Digite seu cupom'),
            ),
          )
        ],
      ),
    );
  }
}
