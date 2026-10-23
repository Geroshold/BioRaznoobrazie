package ru.omgtu.abramov

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import ru.omgtu.abramov.resources.Res
import ru.omgtu.abramov.resources.app_title
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    val title = runBlocking { getString(Res.string.app_title) }
    Window(
        onCloseRequest = ::exitApplication,
        title = title,
    ) {
        App()
    }
}