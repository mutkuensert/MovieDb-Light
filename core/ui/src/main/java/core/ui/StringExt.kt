package core.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

fun String.addClickAction(
    startIndex: Int,
    endIndex: Int,
    style: SpanStyle = SpanStyle(),
    clickableStyle: TextLinkStyles = TextLinkStyles(style.copy(textDecoration = TextDecoration.Underline)),
    action: () -> Unit
): AnnotatedString {
    return buildAnnotatedString {
        append(this@addClickAction)
        addLink(
            LinkAnnotation.Clickable(
                "clickable",
                clickableStyle
            ) { action.invoke() },
            startIndex,
            endIndex
        )
    }
}