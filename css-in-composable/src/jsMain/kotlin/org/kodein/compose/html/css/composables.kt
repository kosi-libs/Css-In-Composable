package org.kodein.compose.html.css

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectScope
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.jetbrains.compose.web.attributes.AttrsScope
import org.jetbrains.compose.web.css.*
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.get


private val isProduction = (js("process.env.NODE_ENV") as String) == "production"

private fun checkCicClasses(element: Element, cssClass: String) {
    val cicClasses = (element.asDynamic().__cic_classes as? HashSet<String>) ?: HashSet<String>().also { element.asDynamic().__cic_classes = it }
    if (cssClass !in cicClasses) {
        cicClasses.add(cssClass)
        if (cicClasses.size == 20) {
            console.warn(
                "The following element has already received 20 different css classes.\n" +
                "This probably means that its css block has frequently changing properties which should be declared in style instead of css."
            )
            console.warn(element)
        }
    }

}

public fun AttrsScope<*>.css(suffix: String? = null, css: CSSBuilder.() -> Unit) {
    val cssClass = cssClass(suffix, css)

    if (!isProduction) {
        prop({ e: HTMLElement, _ -> checkCicClasses(e, cssClass) }, null)
    }

    classes(cssClass)
}


public fun CSSBuilder.animation(
    keyframes: CSSKeyframesBuilder.() -> Unit,
    animation: CSSAnimation.() -> Unit
): Unit = animation(keyframes(null, keyframes), animation)


@Composable
public fun CssEffect(
    element: DisposableEffectScope.() -> Element,
    suffix: String? = null,
    css: CSSBuilder.() -> Unit
) {
    val cssClass = cssClass(suffix, css)

    DisposableEffect(cssClass) {
        val e = element()

        if (!isProduction) {
            checkCicClasses(e, cssClass)
        }

        e.addClass(cssClass)
        onDispose { e.removeClass(cssClass) }
    }
}

@Composable
public fun globalHtmlCss(css: CSSBuilder.() -> Unit): Unit =
    CssEffect(
        element = { document.getElementsByTagName("html")[0]!! },
        suffix = "html",
        css = css
    )
