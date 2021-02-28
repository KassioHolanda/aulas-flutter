import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:mobx_aula2/body.dart';
import 'package:provider/provider.dart';
import 'package:mobx_aula2/controller/controller.dart';

class MyHomePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final controller = Provider.of<Controller>(context);

    return Scaffold(
      appBar: AppBar(
        title: Observer(
          builder: (_) => Text(controller.isVali
              ? 'Formulario Validado'
              : 'Formulario não Validado'),
        ),
      ),
      body: Body(),
    );
  }
}
