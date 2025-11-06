package libraries

import java.util.Locale

object CountryManager {
    val current: String get() = Locale.getDefault().country
    val all: Array<String> get() = Locale.getISOCountries()
}