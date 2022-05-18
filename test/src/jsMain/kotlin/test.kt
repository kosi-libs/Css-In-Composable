import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.coroutines.delay
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposableInBody
import org.kodein.cic.*
import org.w3c.dom.HTMLHtmlElement
import org.w3c.dom.get
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun ColoredDiv(text: String) {
    Div({
        css {
            backgroundColor(Color.blue)
            color(Color.white)
            width(4.em)
            padding(0.2.em)
            margin(0.2.em)
            textAlign("center")
            (self + hover) {
                backgroundColor(Color.red)
            }
        }
    }) {
        Text(text)
    }
}


@Composable
fun Counter() {
    var counter by remember { mutableStateOf(0) }

    Div({
        css {
            fontSize(2.em)
            margin(0.5.em)
            animation(
                keyframes = {
                    to {
                        transform { rotate(360.deg) }
                    }
                },
                animation = {
                    iterationCount(null)
                    duration(2.s)
                    timingFunction(AnimationTimingFunction.Linear)
                }
            )
        }
        style {
            color(hsl(counter % 360, 100, 50))
        }
    }) {
        LaunchedEffect(null) {
            while (true) {
                delay(100.milliseconds)
                ++counter
            }
        }
        Text(counter.toString())
    }
}

@Composable
fun test() {

    globalHtmlCss {
        "*" {
            margin(0.px)
            padding(0.px)
        }
        "body" {
            backgroundColor(Color.silver)
        }
    }

    Div({
        css {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
        }
    }) {
        ColoredDiv("One!")
        ColoredDiv("Two!")

        Counter()
        Counter()
    }
}

fun main() {
    renderComposableInBody { test() }
}
