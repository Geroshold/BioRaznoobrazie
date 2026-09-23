package ru.omgtu.abramov

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BioRaznoobrazie",
    ) {
        App()
    }
}