package core.ui.navigation

import androidx.navigation.NavHostController

class Navigator {
    lateinit var controller: NavHostController
        private set

    fun setNavController(controller: NavHostController) {
        this.controller = controller
    }
}