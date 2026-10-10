package ru.omgtu.abramov.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.browser.window

private fun setCustomLocale(locale: String) {
    js("window.__customLocale = locale")
}

actual object LocalAppLocale {
    private val LocalAppLocale = staticCompositionLocalOf { window.navigator.language }

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val new = value ?: window.navigator.language
        setCustomLocale(new)
        return LocalAppLocale.provides(new)
    }
}