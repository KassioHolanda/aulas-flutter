import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:gerente_loja/blocs/login.dart';
import 'package:gerente_loja/screens/home.dart';
import 'package:gerente_loja/widgets/input_field.dart';

class Login extends StatefulWidget {
  @override
  _LoginState createState() => _LoginState();
}

class _LoginState extends State<Login> {
  final _loginBloc = LoginBloc();

  @override
  void dispose() {
    _loginBloc.dispose();
    super.dispose();
  }

  @override
  void initState() {
    super.initState();
    _loginBloc.outLoginState.listen((state) {
      switch (state) {
        case LoginState.SUCCESS:
          Navigator.of(context)
              .push(MaterialPageRoute(builder: (context) => HomeScreen()));
          break;
        case LoginState.FAIL:
          showDialog(
              context: context,
              builder: (contex) => AlertDialog(
                    title: Text('Error'),
                    content: Text('Voce não possui privilegios necessarios'),
                  ));
          break;
        case LoginState.IDLE:
        case LoginState.LOADING:
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        backgroundColor: Colors.transparent,
      ),
      backgroundColor: Colors.black87,
      body: StreamBuilder<LoginState>(
          stream: _loginBloc.outLoginState,
          initialData: LoginState.LOADING,
          builder: (context, snapshot) {
            print(snapshot.data);
            switch (snapshot.data) {
              case LoginState.LOADING:
                return Center(
                  child: CircularProgressIndicator(),
                );
                break;
              case LoginState.SUCCESS:
              case LoginState.FAIL:
              case LoginState.IDLE:
                return Stack(
                  alignment: Alignment.center,
                  children: <Widget>[
                    Container(),
                    SingleChildScrollView(
                      padding: EdgeInsets.all(16.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.stretch,
                        children: <Widget>[
                          Icon(
                            Icons.store_mall_directory,
                            color: Colors.pinkAccent,
                            size: 160,
                          ),
                          InputField(
                            icon: Icons.person_outline,
                            hint: 'Email',
                            obscure: false,
                            stream: _loginBloc.outEmail,
                            onChanged: _loginBloc.changeEmail,
                          ),
                          InputField(
                            icon: Icons.lock_outline,
                            hint: 'Senha',
                            obscure: true,
                            stream: _loginBloc.outSenha,
                            onChanged: _loginBloc.changeSenha,
                          ),
                          SizedBox(
                            height: 32,
                          ),
                          StreamBuilder<bool>(
                              stream: _loginBloc.outSubmitValid,
                              builder: (context, snapshot) {
                                return SizedBox(
                                  height: 50,
                                  child: RaisedButton(
                                    color: Colors.pink,
                                    disabledColor: Colors.pink[100],
                                    disabledTextColor: Colors.white,
                                    textColor: Colors.white,
                                    onPressed: snapshot.hasData
                                        ? _loginBloc.submit
                                        : null,
                                    child: Text('Entrar'),
                                  ),
                                );
                              })
                        ],
                      ),
                    ),
                  ],
                );
                break;
            }
          }),
    );
  }
}
