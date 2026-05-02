package core.ui

import core.ui.navigation.Navigator
import org.koin.dsl.module

val uiModule = module {
    single { Navigator() }
    single { StatusBarBackgroundColorHandler() }
    single { LoadingAnimator() }
    single { PopupHandler(get()) }
}