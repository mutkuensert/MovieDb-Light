package core.data.image

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

interface TmdbImage {
    val path: String
    val originalSizedUrl: String get() = IMAGE_BASE_URL + "original" + path

    fun getSizedUrl(size: String): String = IMAGE_BASE_URL + size + path
}

@Suppress("unused")
class Poster(override val path: String) : TmdbImage {
    val w92Url get() = getSizedUrl("w92")
    val w154Url get() = getSizedUrl("w154")
    val w185Url get() = getSizedUrl("w185")
    val w342Url get() = getSizedUrl("w342")
    val w500Url get() = getSizedUrl("w500")
    val w780Url get() = getSizedUrl("w780")
}

@Suppress("unused")
class Backdrop(override val path: String) : TmdbImage {
    val w300Url get() = getSizedUrl("w300")
    val w780Url get() = getSizedUrl("w780")
    val w1280Url get() = getSizedUrl("w1280")
}

@Suppress("unused")
class Logo(override val path: String) : TmdbImage {
    val w45Url get() = getSizedUrl("w45")
    val w92Url get() = getSizedUrl("w92")
    val w154Url get() = getSizedUrl("w154")
    val w185Url get() = getSizedUrl("w185")
    val w300Url get() = getSizedUrl("w300")
    val w500Url get() = getSizedUrl("w500")
}

@Suppress("unused")
class Profile(override val path: String) : TmdbImage {
    val w45Url get() = getSizedUrl("w45")
    val w185Url get() = getSizedUrl("w185")
    val h632Url get() = getSizedUrl("h632")
}

@Suppress("unused")
class Still(override val path: String) : TmdbImage {
    val w92Url get() = getSizedUrl("w92")
    val w185Url get() = getSizedUrl("w185")
    val w300Url get() = getSizedUrl("w300")
}
