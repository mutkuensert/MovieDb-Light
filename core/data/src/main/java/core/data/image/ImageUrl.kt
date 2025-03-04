package core.data.image

object ImageUrl {
    fun create(path: String, size: ImageSize = ImageSize.SIZE_ORIGINAL): String {
        return IMAGE_BASE_URL + size.sizePath + path
    }
}

enum class ImageSize(val sizePath: String) {
    POSTER_SIZE_W500("w500"),
    SIZE_ORIGINAL("original")
}

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"