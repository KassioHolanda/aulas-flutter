import 'package:mobx/mobx.dart';
part 'client.g.dart';

class Client = _ClientBase with _$Client;

abstract class _ClientBase with Store {

  @observable
  String nome;
  @observable
  String cpf;
  @observable
  String email;

  @action
  changeName(String newValue) => nome = newValue;
  @action
  changeEmail(String newValue) => email = newValue;
  @action
  changeCPF(String newValue) => cpf = newValue;


}