package ru.omgtu.abramov.utils

internal fun displayTaxonNumber(key: Int): String = key.toString().padStart(3, '0')

internal fun displayTaxonName(name: String): String = name.replaceFirstChar { it.uppercase() }

fun formatNumDescendants(value: Int): String {
    val str = value.toString()
    val sb = StringBuilder()
    for (i in str.indices) {
        if (i > 0 && (str.length - i) % 3 == 0) sb.append(' ')
        sb.append(str[i])
    }
    return sb.toString()
}