package moviedblight.core.injection.commonmodules.core

import moviedblight.core.ui.navigation.Navigator
import org.koin.dsl.module

val uiModule = module {
    single { Navigator() }
}