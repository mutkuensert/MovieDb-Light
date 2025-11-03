package core.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PopupHandler {
    private val _popup = MutableStateFlow<PopupConfig?>(null)
    val popup = _popup.asStateFlow()


    fun show(popupConfig: PopupConfig) {
        _popup.value = popupConfig
    }

    fun showSimpleMessage(message: String) {
        _popup.value = PopupConfig(message = message)
    }

    fun close() {
        _popup.value = null
    }
}

data class PopupConfig(
    val title: String? = null,
    val message: String = "",
    val showConfirmButton: Boolean = true,
    val confirmButtonText: String = "",
    val onConfirm: () -> Unit = {},
    val showDismissButton: Boolean = false,
    val dismissButtonText: String = "",
    val onDismiss: () -> Unit = {},
    val allowToDismiss: Boolean = true,
)
