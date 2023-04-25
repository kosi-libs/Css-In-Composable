import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposableInBody
import org.kodein.compose.html.css.animation
import org.kodein.compose.html.css.css
import org.kodein.compose.html.css.globalHtmlCss
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun BadUsage() {
    var add: Double by remember { mutableStateOf(0.0) }
    Button({
        css {
            fontSize((1 + add).cssRem)
        }
        onClick { add += 0.05 }
    }) { Text("Click me!") }
}

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
        BadUsage()
    }
}

fun main() {
    renderComposableInBody { test() }
}
