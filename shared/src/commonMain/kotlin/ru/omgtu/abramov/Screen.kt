package ru.omgtu.abramov

sealed interface Screen {
    data object List : Screen
    data class Detail(val key: Int) : Screen
    data class Children(val parentKey: Int, val parentName: String) : Screen
}