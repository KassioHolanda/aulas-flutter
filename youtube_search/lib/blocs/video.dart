import 'package:bloc_pattern/bloc_pattern.dart';
import 'package:youtube_search/api.dart';
import 'package:youtube_search/models/video.dart';
import 'dart:async';

class VideoBloc implements BlocBase {
  Api api;
  List<Video> videos;
  final StreamController<List<Video>> _videosController =
      StreamController<List<Video>>();
  final StreamController<String> _searchController = StreamController<String>();

  Stream get outVideos => _videosController.stream;

  Sink get inSearch => _searchController.sink;

  VideoBloc() {
    api = Api();
    _searchController.stream.listen(_search);
  }

  void _search(String search) async {
    if (search != null) {
      _videosController.sink.add([]);
      videos = await api.search(search);
    } else {
      videos += await api.nextpage();
    }
    _videosController.sink.add(videos);
  }

  @override
  void dispose() {
    _videosController.close();
    _searchController.close();
  }
}
