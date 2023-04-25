package org.kodein.cic

import org.jetbrains.compose.web.css.*


internal fun areKeyframesEquivalent(
    lClass: String, lKeyframes: CSSKeyframeRuleDeclarationList,
    rClass: String, rKeyframes: CSSKeyframeRuleDeclarationList
): Boolean {
    if (lKeyframes.size != rKeyframes.size) return false

    repeat(lKeyframes.size) {
        val lKeyframe = lKeyframes[it]
        val rKeyframe = rKeyframes[it]
        if (lKeyframe.header != rKeyframe.header.replace(rClass, lClass)) return false
        if (lKeyframe.style != rKeyframe.style) return false
    }
    return true
}

internal fun areRulesEquivalent(
    lClass: String, lRules: CSSRuleDeclarationList,
    rClass: String, rRules: CSSRuleDeclarationList
): Boolean {
    if (lRules.size != rRules.size) return false
    repeat(lRules.size) {
        val lRule = lRules[it]
        val rRule = rRules[it]
        if (lRule.header != rRule.header.replace(rClass, lClass)) return false
        if (lRule::class != rRule::class) return false
        when (lRule) {
            is CSSStyleRuleDeclaration -> {
                rRule as CSSStyleRuleDeclaration
                if (lRule.style != rRule.style) return false
            }
            is CSSMediaRuleDeclaration -> {
                rRule as CSSMediaRuleDeclaration
                if (!areRulesEquivalent(lClass, lRule.rules, rClass, rRule.rules)) return false
            }
            is CSSKeyframesRuleDeclaration -> {
                rRule as CSSKeyframesRuleDeclaration
                if (!areKeyframesEquivalent(lClass, lRule.keys, rClass, rRule.keys)) return false
            }
            is CSSKeyframeRuleDeclaration -> {
                rRule as CSSKeyframeRuleDeclaration
                if (lRule.style != rRule.style) return false
            }
            else -> {
                console.error("Unknown CSSRuleDeclaration type ${lRule::class}")
                return false
            }
        }
    }
    return true
}
