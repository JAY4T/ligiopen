package com.admin.ligiopen.utils

fun intFilter(value: String): String {
    return value.filter { it.isDigit() }
}