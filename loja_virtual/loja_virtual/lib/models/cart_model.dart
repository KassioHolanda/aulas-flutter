import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:loja_virtual/datas/cart_product.dart';
import 'package:loja_virtual/models/user_model.dart';
import 'package:scoped_model/scoped_model.dart';

class CartModel extends Model {
  UserModel userModel;
  List<CartProduct> products = [];
  bool isLoading = false;

  String cupomCode;
  int discountPercentage = 0;

  CartModel(this.userModel) {
    if (userModel.isLoggedIn()) {
      _loadCartItens();
    }
  }

  void addCartItem(CartProduct product) {
    products.add(product);
    Firestore.instance
        .collection('users')
        .document(userModel.firebaseUser.uid)
        .collection('cart')
        .add(product.toMap())
        .then((data) {
      product.cid = data.documentID;
    });
    notifyListeners();
  }

  void removeCartItem(CartProduct product) {
    Firestore.instance
        .collection('users')
        .document(userModel.firebaseUser.uid)
        .collection('cart')
        .document(product.cid)
        .delete();
    products.remove(product);
    notifyListeners();
  }

  static CartModel of(BuildContext context) {
    return ScopedModel.of<CartModel>(context);
  }

  void decProduct(CartProduct cartProduct) {
    cartProduct.quantity--;
    Firestore.instance
        .collection('users')
        .document(userModel.firebaseUser.uid)
        .collection('cart')
        .document(cartProduct.cid)
        .updateData(cartProduct.toMap());
    notifyListeners();
  }

  void incProduct(CartProduct cartProduct) {
    cartProduct.quantity++;
    Firestore.instance
        .collection('users')
        .document(userModel.firebaseUser.uid)
        .collection('cart')
        .document(cartProduct.cid)
        .updateData(cartProduct.toMap());
    notifyListeners();
  }

  void _loadCartItens() async {
    QuerySnapshot querySnapshot = await Firestore.instance
        .collection('users')
        .document(userModel.firebaseUser.uid)
        .collection('cart')
        .getDocuments();
    products = querySnapshot.documents.map((doc) {
      return CartProduct.fromDocument(doc);
    }).toList();
    notifyListeners();
  }

  void setCoupon(String code, int discountPercentage) {
    this.cupomCode = code;
    this.discountPercentage = discountPercentage;
  }

  double getProductsPrice() {
    double price = 0.0;
    for (CartProduct c in this.products) {
      if (c.productData != null) {
        price += c.quantity * c.productData.price;
      }
    }
    return price;
  }

  double getShipPrice() {
    return 9.99;
  }

  double getDiscount() {
    return getProductsPrice() * discountPercentage / 100;
  }

  void updatePrices() {
    notifyListeners();
  }

  Future<String> finishOrder() async {
    if (products.length == 0) {
      return null;
    } else {
      isLoading = true;
      notifyListeners();

      DocumentReference refOrder =
          await Firestore.instance.collection('orders').add({
        'clientId': userModel.firebaseUser.uid,
        'products': products.map((cartProduct) => cartProduct.toMap()).toList(),
        'shipPrice': getShipPrice(),
        'productsPrice': getProductsPrice(),
        'discount': getDiscount(),
        'totalPrice': getProductsPrice() - getDiscount() + getShipPrice(),
        'status': 1,
      });

      await Firestore.instance
          .collection('users')
          .document(userModel.firebaseUser.uid)
          .collection('orders')
          .document(refOrder.documentID)
          .setData({'orderId': refOrder.documentID});

      QuerySnapshot querySnapshot = await Firestore.instance
          .collection('users')
          .document(userModel.firebaseUser.uid)
          .collection('cart')
          .getDocuments();

      for (DocumentSnapshot doc in querySnapshot.documents) {
        doc.reference.delete();
      }

      products.clear();

      discountPercentage = 0;
      cupomCode = null;
      isLoading = false;
      notifyListeners();

      return refOrder.documentID;
    }
  }
}
