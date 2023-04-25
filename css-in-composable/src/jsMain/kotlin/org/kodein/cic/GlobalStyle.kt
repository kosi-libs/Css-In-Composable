package org.kodein.cic

import kotlinx.browser.document
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.internal.runtime.ComposeWebInternalApi
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.css.*


private external class CSSKeyframesRule: CSSRule {
    val cssRules: CSSRuleList
}

@OptIn(ComposeWebInternalApi::class)
private fun fillRule(
    cssRuleDeclaration: CSSRuleDeclaration,
    cssRule: CSSRule
) {
    when (cssRuleDeclaration) {
        is CSSStyledRuleDeclaration -> {
            val cssStyleRule = cssRule.unsafeCast<CSSStyleRule>()
            cssRuleDeclaration.style.properties.forEach { (name, value) ->
                cssStyleRule.style.setProperty(name, value.toString())
            }
            cssRuleDeclaration.style.variables.forEach { (name, value) ->
                cssStyleRule.style.setProperty("--$name", value.toString())
            }
        }
        is CSSGroupingRuleDeclaration -> {
            val cssGroupingRule = cssRule.unsafeCast<CSSGroupingRule>()
            cssRuleDeclaration.rules.forEach { childRuleDeclaration ->
                val cssRuleIndex = cssGroupingRule.insertRule("${childRuleDeclaration.header} {}", cssGroupingRule.cssRules.length)
                cssGroupingRule.cssRules.item(cssRuleIndex)?.let { fillRule(childRuleDeclaration, it) }
            }
        }
        is CSSKeyframesRuleDeclaration -> {
            val cssGroupingRule = cssRule.unsafeCast<CSSKeyframesRule>()
            cssRuleDeclaration.keys.forEach { childRuleDeclaration ->
                cssGroupingRule.asDynamic().appendRule("${childRuleDeclaration.header} {}")
                cssGroupingRule.cssRules.item(cssGroupingRule.cssRules.length - 1)?.let { fillRule(childRuleDeclaration, it) }
            }
        }
    }
}

private object InHeadRulesHolder : CSSRulesHolder {
    private val style: HTMLStyleElement = document.createElement("style") as HTMLStyleElement
    private val rules = ArrayList<CSSRuleDeclaration>()
    override val cssRules: CSSRuleDeclarationList get() = rules

    init {
        document.head!!.append(style)
    }

    override fun add(cssRule: CSSRuleDeclaration) {
        rules.add(cssRule)

        (style.sheet as? CSSStyleSheet)?.let { cssStylesheet ->
            repeat(cssStylesheet.cssRules.length) {
                cssStylesheet.deleteRule(0)
            }
            cssRules.forEach { cssRule ->
                val cssRuleIndex = cssStylesheet.insertRule("${cssRule.header} {}", cssStylesheet.cssRules.length)
                cssStylesheet.cssRules.item(cssRuleIndex)?.let { fillRule(cssRule, it) }
            }

        }
    }
}

public object GlobalStyle : StyleSheet(InHeadRulesHolder, false)

public fun cssClass(suffix: String? = null, css: CSSBuilder.() -> Unit): String = GlobalStyle.css(suffix, css)

public fun keyframes(suffix: String? = null, keyframes: CSSKeyframesBuilder.() -> Unit): String = GlobalStyle.keyframes(suffix, keyframes)
