import 'package:flutter/material.dart';
import 'package:flutter_app/models/item.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: true,
      theme: ThemeData(
        primarySwatch: Colors.green,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  var itens = new List<Item>();

  MyHomePage() {
    itens = [];
    itens.add(Item(title: "Item 1", done: false));
    itens.add(Item(title: "Item 2", done: true));
    itens.add(Item(title: "Item 3", done: false));

  }

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Todo App")),
      body: ListView.builder(
          itemCount: widget.itens.length,
          itemBuilder: (BuildContext ctxt, int index) {
            final item = widget.itens[index];

            return CheckboxListTile(
              title: Text(item.title),
              key: Key(item.title),
              value: item.done,
              onChanged: (value) {
                setState(() {
                  item.done = value;
                });
              },
            );
          }),
    );
  }
}
