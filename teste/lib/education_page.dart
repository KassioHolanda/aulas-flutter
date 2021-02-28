import 'package:flutter/material.dart';
import 'package:teste/constantes.dart';
import 'package:teste/icon_container.dart';

class EducationDarkCardContent extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Icon(
      Icons.laptop_mac,
      color: kWhite,
      size: 96.0,
    );
  }
}

class EducationLightCardContent extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: <Widget>[
        IconContainer(
          icon: Icons.brush,
          padding: kPaddingS,
        ),
        IconContainer(
          icon: Icons.camera_alt,
          padding: kPaddingM,
        ),
        IconContainer(
          icon: Icons.straighten,
          padding: kPaddingS,
        ),
      ],
    );
  }
}

class EducationTextColumn extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return TextColumn(
      title: 'Keep learning',
      text: 'Ipsum magna enim cupidatat culpa elit cillum velit occaecat.',
    );
  }
}
