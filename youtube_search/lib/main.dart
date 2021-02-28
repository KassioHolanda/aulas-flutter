import 'package:bloc_pattern/bloc_pattern.dart';
import 'package:flutter/material.dart';
import 'package:youtube_search/blocs/favorite.dart';
import 'package:youtube_search/blocs/video.dart';
import 'package:youtube_search/screens/home.dart';
import 'package:youtube_search/api.dart';

import 'api.dart';

void main() {
//  Api api = Api();
//  api.search('eletro');
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      bloc: VideoBloc(),
      child: BlocProvider(
        bloc: FavoriteBloc(),
        child: MaterialApp(
          debugShowCheckedModeBanner: false,
          title: 'Youtube Search',
          theme: ThemeData(
            primarySwatch: Colors.blue,
          ),
          home: Home(),
        ),
      ),
    );
  }
}
