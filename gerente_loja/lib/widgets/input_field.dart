import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class InputField extends StatelessWidget {
  final IconData icon;
  final String hint;
  final bool obscure;
  final Stream<String> stream;
  final Function(String) onChanged;

  InputField({this.icon, this.hint, this.obscure, this.stream, this.onChanged});

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<String>(
        stream: stream,
        builder: (context, snapshot) {
          return TextField(
            onChanged: onChanged,
            style: TextStyle(color: Colors.white),
            obscureText: obscure,
            decoration: InputDecoration(
              errorText: snapshot.hasError ? snapshot.error : null,
              hintText: hint,
              icon: Icon(
                icon,
                color: Colors.white,
              ),
              contentPadding:
                  EdgeInsets.only(left: 5, right: 16, bottom: 16, top: 16),
              hintStyle: TextStyle(color: Colors.white),
              focusedBorder: UnderlineInputBorder(
                  borderSide: BorderSide(color: Colors.pink)),
            ),
          );
        });
  }
}
