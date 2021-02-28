class OnboardingPageIndicator extends StatelessWidget {
  final int currentPage;
  final Widget child;

  const OnboardingPageIndicator({
    @required this.currentPage,
    @required this.child,
  })  : assert(currentPage != null),
        assert(child != null);

  Color _getPageIndicatorColor(int pageIndex) {
    return currentPage > pageIndex ? kWhite : kWhite.withOpacity(0.25);
  }

  double get indicatorGap => pi / 12;
  double get indicatorLength => pi / 3;

  @override
  Widget build(BuildContext context) {
    return Container(
      child: CustomPaint(
        painter: _OnboardignPageIndicatorPainter(
          color: _getPageIndicatorColor(0),
          startAngle: (4 * indicatorLength) - (indicatorLength + indicatorGap),
          indicatorLength: indicatorLength,
        ),
        child: CustomPaint(
          painter: _OnboardignPageIndicatorPainter(
            color: _getPageIndicatorColor(1),
            startAngle: 4 * indicatorLength,
            indicatorLength: indicatorLength,
          ),
          child: CustomPaint(
            painter: _OnboardignPageIndicatorPainter(
              color: _getPageIndicatorColor(2),
              startAngle:
                  (4 * indicatorLength) + (indicatorLength + indicatorGap),
              indicatorLength: indicatorLength,
            ),
            child: child,
          ),
        ),
      ),
    );
  }
}
