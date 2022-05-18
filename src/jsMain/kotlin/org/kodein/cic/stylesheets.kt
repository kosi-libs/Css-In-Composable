package org.kodein.cic

import org.jetbrains.compose.web.css.*
import kotlin.reflect.KProperty


private class FakeProperty(n: String) : KProperty<String> {
    init {
        this.asDynamic().callableName = n
    }
    override val name: String get() = this.asDynamic().callableName as String
}

private class SimpleRulesHolder : CSSRulesHolder {
    override val cssRules = ArrayList<CSSRuleDeclaration>()
    override fun add(cssRule: CSSRuleDeclaration) { cssRules += cssRule }
}

private class CICStylesheet private constructor(
    val name: String,
) : StyleSheet(SimpleRulesHolder(), usePrefix = false) {

    companion object {
        fun style(
            name: String,
            cssBuilder: CSSBuilder.() -> Unit
        ) = CICStylesheet(name).apply {
            val property = FakeProperty(name)
            style(cssBuilder)
                .provideDelegate(this, property)
                .getValue(this, property)
        }

        fun keyframes(
            name: String,
            cssKeyframes: CSSKeyframesBuilder.() -> Unit
        ) = CICStylesheet(name).apply {
            val property = FakeProperty(name)
            keyframes(cssKeyframes)
                .provideDelegate(this, property)
                .getValue(this, property)
        }
    }
}

private fun StyleSheet.addRulesIfNew(sheets: MutableList<CICStylesheet>, new: CICStylesheet): String {
    val existing = sheets.find { areRulesEquivalent(new.name, new.cssRules, it.name, it.cssRules) }
    if (existing != null) {
        return existing.name
    }

    sheets += new

    new.cssRules.forEach { add(it) }

    return new.name
}

private val cssSheets = ArrayList<CICStylesheet>()

public fun StyleSheet.css(suffix: String? = null, css: CSSBuilder.() -> Unit): String {
    val s = if (suffix != null) "-$suffix" else ""
    val new = CICStylesheet.style("css-${cssSheets.size}$s", css)

    return addRulesIfNew(cssSheets, new)
}

private val keyframesSheets = ArrayList<CICStylesheet>()

public fun StyleSheet.keyframes(suffix: String? = null, keyframes: CSSKeyframesBuilder.() -> Unit): String {
    val s = if (suffix != null) "-$suffix" else ""
    val new = CICStylesheet.keyframes("kf-${keyframesSheets.size}$s", keyframes)

    return addRulesIfNew(keyframesSheets, new)
}
