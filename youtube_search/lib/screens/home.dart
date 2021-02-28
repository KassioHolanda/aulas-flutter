import 'package:bloc_pattern/bloc_pattern.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:youtube_search/blocs/favorite.dart';
import 'package:youtube_search/blocs/video.dart';
import 'package:youtube_search/delegates/data_search.dart';
import 'package:youtube_search/models/video.dart';
import 'package:youtube_search/screens/favorites.dart';
import 'package:youtube_search/widgets/video_tile.dart';

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black87,
        elevation: 0,
        actions: <Widget>[
          Align(
            alignment: Alignment.center,
            child: StreamBuilder<Map<String, Video>>(
              builder: (context, snaphot) {
                if (snaphot.hasData) {
                  return Text('${snaphot.data.length}');
                } else {
                  return Container();
                }
              },
              stream: BlocProvider.of<FavoriteBloc>(context).outFav,
            ),
          ),
          IconButton(
            onPressed: () {
              Navigator.of(context).push(
                  MaterialPageRoute(builder: (context) => Favorites()));
            },
            icon: Icon(
              Icons.star,
              color: Colors.white,
            ),
          ),
          IconButton(
            onPressed: () async {
              String result =
                  await showSearch(context: context, delegate: DataSearch());
              if (result != null) {
                print(result);
                BlocProvider.of<VideoBloc>(context).inSearch.add(result);
              }

//              print(result);
            },
            icon: Icon(
              Icons.search,
              color: Colors.white,
            ),
          ),
        ],
        title: Container(
          height: 45.0,
          child: Image.asset('images/youtube_logo.png'),
        ),
      ),
      body: StreamBuilder(
        stream: BlocProvider.of<VideoBloc>(context).outVideos,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            return ListView.builder(
              itemBuilder: (context, index) {
                if (index < snapshot.data.length) {
                  return VideoTile(snapshot.data[index]);
                } else if (index > 1) {
                  BlocProvider.of<VideoBloc>(context).inSearch.add(null);
                  return Container(
                    margin: EdgeInsets.all(10),
                    height: 40,
                    width: 40,
                    alignment: Alignment.center,
                    child: CircularProgressIndicator(),
                  );
                }
              },
              itemCount: snapshot.data.length + 1,
            );
          } else {
            return Container();
          }
        },
      ),
    );
  }
}
