// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'controller.dart';

// **************************************************************************
// StoreGenerator
// **************************************************************************

// ignore_for_file: non_constant_identifier_names, unnecessary_lambdas, prefer_expression_function_bodies, lines_longer_than_80_chars, avoid_as, avoid_annotating_with_dynamic

mixin _$HomeController on _HomeControllerBase, Store {
  Computed<List<ItemModel>> _$getFilteredListComputed;

  @override
  List<ItemModel> get getFilteredList => (_$getFilteredListComputed ??=
          Computed<List<ItemModel>>(() => super.getFilteredList))
      .value;
  Computed<int> _$totalCheckedComputed;

  @override
  int get totalChecked =>
      (_$totalCheckedComputed ??= Computed<int>(() => super.totalChecked))
          .value;

  final _$listItensAtom = Atom(name: '_HomeControllerBase.listItens');

  @override
  ObservableList<ItemModel> get listItens {
    _$listItensAtom.context.enforceReadPolicy(_$listItensAtom);
    _$listItensAtom.reportObserved();
    return super.listItens;
  }

  @override
  set listItens(ObservableList<ItemModel> value) {
    _$listItensAtom.context.conditionallyRunInAction(() {
      super.listItens = value;
      _$listItensAtom.reportChanged();
    }, _$listItensAtom, name: '${_$listItensAtom.name}_set');
  }

  final _$filterAtom = Atom(name: '_HomeControllerBase.filter');

  @override
  String get filter {
    _$filterAtom.context.enforceReadPolicy(_$filterAtom);
    _$filterAtom.reportObserved();
    return super.filter;
  }

  @override
  set filter(String value) {
    _$filterAtom.context.conditionallyRunInAction(() {
      super.filter = value;
      _$filterAtom.reportChanged();
    }, _$filterAtom, name: '${_$filterAtom.name}_set');
  }

  final _$_HomeControllerBaseActionController =
      ActionController(name: '_HomeControllerBase');

  @override
  dynamic addItem(ItemModel item) {
    final _$actionInfo = _$_HomeControllerBaseActionController.startAction();
    try {
      return super.addItem(item);
    } finally {
      _$_HomeControllerBaseActionController.endAction(_$actionInfo);
    }
  }

  @override
  dynamic setFilter(String newValue) {
    final _$actionInfo = _$_HomeControllerBaseActionController.startAction();
    try {
      return super.setFilter(newValue);
    } finally {
      _$_HomeControllerBaseActionController.endAction(_$actionInfo);
    }
  }

  @override
  dynamic removeItem(ItemModel model) {
    final _$actionInfo = _$_HomeControllerBaseActionController.startAction();
    try {
      return super.removeItem(model);
    } finally {
      _$_HomeControllerBaseActionController.endAction(_$actionInfo);
    }
  }

  @override
  String toString() {
    final string =
        'listItens: ${listItens.toString()},filter: ${filter.toString()},getFilteredList: ${getFilteredList.toString()},totalChecked: ${totalChecked.toString()}';
    return '{$string}';
  }
}
