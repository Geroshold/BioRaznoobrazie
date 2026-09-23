package ru.omgtu.abramov.ui.model

internal fun displayTaxonNumber(key: Int): String = key.toString().padStart(3, '0')

internal fun displayTaxonName(name: String): String = name.replaceFirstChar { it.uppercase() }