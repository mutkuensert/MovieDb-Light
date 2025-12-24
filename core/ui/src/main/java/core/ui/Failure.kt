package core.ui

import core.domain.Failure

fun PopupHandler.showFailurePopup(failure: Failure) {
    showSimpleMessage(failure.message)
}