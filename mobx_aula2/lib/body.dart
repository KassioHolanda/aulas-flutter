import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:provider/provider.dart';
import 'package:mobx_aula2/controller/controller.dart';

class Body extends StatelessWidget {
_textFiel({String text, onChanged, String textErro}) {
    return TextField(
      decoration: InputDecoration(
        border: OutlineInputBorder(),
        labelText: text,
        errorText: textErro == null ? null : textErro,
      ),
      onChanged: onChanged,
    );
  }

  @override
  Widget build(BuildContext context) {

    final controller = Provider.of<Controller>(context);

    return Padding(
        padding: EdgeInsets.all(10),
        child: Column(
          children: <Widget>[
            Observer(
              builder: (_) {
                return _textFiel(
                    text: 'Nome',
                    onChanged: controller.client.changeName,
                    textErro: controller.validadeName());
              },
            ),
            SizedBox(
              height: 10,
            ),
            Observer(
              builder: (_) {
                return _textFiel(
                    text: 'Email',
                    onChanged: controller.client.changeEmail,
                    textErro: controller.validadeEmail());
              },
            ),
            SizedBox(
              height: 10,
            ),
            Observer(
              builder: (_) {
                return _textFiel(
                    text: 'CPF', onChanged: controller.client.changeCPF);
              },
            ),
            SizedBox(
              height: 10,
            ),
            Observer(
              builder: (c) {
                return RaisedButton(
                  onPressed: controller.isVali ? () {} : null,
                  child: Text('Salvar'),
                );
              },
            )
          ],
        ),
      );
  }
}