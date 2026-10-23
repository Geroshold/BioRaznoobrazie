package ru.omgtu.abramov.ui

import androidx.compose.runtime.*

var customAppLocale by mutableStateOf<String?>(null)

expect object LocalAppLocale {
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}

@Composable
fun AppEnvironment(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppLocale provides customAppLocale,
    ) {
        key(customAppLocale) {
            content()
        }
    }
}