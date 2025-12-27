package feature.search.domain

sealed class SearchResult() {
    class Movie() : SearchResult()
    class TvShow() : SearchResult()
    class Person() : SearchResult()
}