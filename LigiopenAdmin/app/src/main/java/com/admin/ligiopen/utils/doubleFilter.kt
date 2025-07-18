package com.admin.ligiopen.utils

fun doubleFilter(value: String): String {
    return value.filter { it.isDigit() || it == '.' }
}