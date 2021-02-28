import 'package:flutter/material.dart';

class DrawerTile extends StatelessWidget {
  final IconData icon;
  final String text;
  final PageController controller;
  final int page;

  DrawerTile(this.icon, this.text, this.controller, this.page);

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        onTap: () {
          Navigator.of(context).pop();
          controller.jumpToPage(page);
        },
        child: Container(
          height: 50.0,
          child: Row(
            children: <Widget>[
              Icon(
                icon,
                size: 22.0,
                color: controller.page.round() == page
                    ? Colors.amber
                    : Colors.white,
              ),
              SizedBox(
                width: 16.0,
              ),
              Text(text,
                  style: TextStyle(
                    fontSize: 16.0,
                    color: controller.page.round() == page
                        ? Colors.amber
                        : Colors.white,
                  ))
            ],
          ),
        ),
      ),
    );
  }
}