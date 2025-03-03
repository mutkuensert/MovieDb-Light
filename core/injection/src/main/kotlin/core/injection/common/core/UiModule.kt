package core.injection.common.core

import core.ui.navigation.Navigator
import org.koin.dsl.module

val uiModule = module {
    single { Navigator() }
}