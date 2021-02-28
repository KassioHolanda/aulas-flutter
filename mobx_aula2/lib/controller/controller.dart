import 'package:mobx/mobx.dart';
import 'package:mobx_aula2/models/client.dart';
part 'controller.g.dart';

class Controller = _ControllerBase with _$Controller;

abstract class _ControllerBase with Store {
  var client = Client();

  @computed
  bool get isVali {
    return validadeEmail() == null && validadeName() == null;
  }

  String validadeName() {
    if (client.nome == null || client.nome.isEmpty) {
      return 'Este campo é obrigatorio';
    }

    if (client.nome.length <= 3) {
      return 'Nome invalido';
    }

    return null;
  }


  String validadeEmail() {
    if (client.email == null || client.email.isEmpty) {
      return 'Este campo é obrigatorio';
    }

    if (!client.email.contains('@')) {
      return 'Email invalido';
    }

    return null;
  }

  dispose() {

  }
}
