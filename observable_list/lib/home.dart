import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:observable_list/components/controller.dart';
import 'package:observable_list/item_widget.dart';
import 'package:observable_list/models/item_model.dart';

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final controller = HomeController();

    _dialog() {
      var model = ItemModel();

      showDialog(
          context: context,
          builder: (_) {
            return AlertDialog(
              title: Text('Adicionar Item'),
              content: TextField(
                onChanged: model.setTitle,
                decoration: InputDecoration(
                  border: OutlineInputBorder(),
                  labelText: 'Novo item',
                ),
              ),
              actions: <Widget>[
                FlatButton(
                  onPressed: () {
                    controller.addItem(model);
                    Navigator.pop(context);
                  },
                  child: Text('Salvar'),
                ),
                FlatButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: Text('Cancelar'),
                )
              ],
            );
          });
    }

    return Scaffold(
      appBar: AppBar(
        title: TextField(
          onChanged: controller.setFilter,
          decoration: InputDecoration(hintText: 'Pesquisar...'),
        ),
        actions: <Widget>[
          IconButton(onPressed: (){},icon: Observer(builder: (_) {
             return Text('${controller.totalChecked}');
          },
          ),),
        ],
      ),
      body: Observer(
        builder: (_) {
          return ListView.builder(
              itemCount: controller.getFilteredList.length,
              itemBuilder: (_, index) {
                var item = controller.getFilteredList[index];
                return ItemWidget(
                  item: item,
                  removeClicked: () {
                    controller.removeItem(item);
                  },
                );
              });
        },
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: () => _dialog(),
      ),
    );
  }
}
