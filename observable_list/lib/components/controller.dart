import 'package:mobx/mobx.dart';
import 'package:observable_list/models/item_model.dart';
part 'controller.g.dart';

class HomeController = _HomeControllerBase with _$HomeController;

abstract class _HomeControllerBase with Store {
  @observable
  ObservableList<ItemModel> listItens = [
    ItemModel(title: 'Item 1', check: true),
    ItemModel(title: 'Item 2', check: false),
    ItemModel(title: 'Item 3', check: false),
  ].asObservable();

  @action
  addItem(ItemModel item) {
    listItens.add(item);
  }

  @observable
  String filter = '';

  @action
  setFilter(String newValue) => filter = newValue;

  @computed
  List<ItemModel> get getFilteredList {
    if (filter.isEmpty) {
      return listItens;
    } else {
      return listItens.where((item) => item.title.toLowerCase().contains(filter.toLowerCase())).toList();
    }
  }

  @computed
  int get totalChecked => listItens.where((item) => item.check).length;

  @action
  removeItem(ItemModel model) {
    listItens.removeWhere((item) => item.title == model.title);
  }
}
