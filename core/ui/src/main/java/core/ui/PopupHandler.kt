package core.ui

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import utils.stringresource.StringResource

class PopupHandler(private val stringResource: StringResource) {
    private val _popup = MutableStateFlow<Popup?>(null)
    val popup = _popup.asStateFlow()


    fun show(popup: Popup.Builder.() -> Unit) {
        _popup.value = Popup.Builder(stringResource).apply(popup).build()
    }

    fun showSimpleMessage(message: String) {
        val builder = Popup.Builder(stringResource)
        builder.message = message
        _popup.value = builder.build()
    }

    fun showSimpleMessage(@StringRes stringResId: Int) {
        val builder = Popup.Builder(stringResource)
        builder.message = stringResource.get(stringResId)
        _popup.value = builder.build()
    }

    fun close() {
        _popup.value = null
    }
}

data class Popup(
    val title: String?,
    val message: String,
    val showConfirmButton: Boolean,
    val confirmButtonText: String,
    val onConfirm: () -> Unit,
    val showDismissButton: Boolean,
    val dismissButtonText: String,
    val onDismiss: () -> Unit,
    val allowToDismiss: Boolean,
) {
    class Builder(stringResource: StringResource) {
        var title: String? = null
        var message: String = ""
        var showConfirmButton: Boolean = true
        var confirmButtonText: String = stringResource.get(R.string.ok)
        var onConfirm: () -> Unit = {}
        var showDismissButton: Boolean = false
        var dismissButtonText: String = stringResource.get(R.string.cancel)
        var onDismiss: () -> Unit = {}
        var allowToDismiss: Boolean = true

        fun build(): Popup {
            return Popup(
                title,
                message,
                showConfirmButton,
                confirmButtonText,
                onConfirm,
                showDismissButton,
                dismissButtonText,
                onDismiss,
                allowToDismiss,
            )
        }
    }
}
