package com.example.weather.extensions

fun Float.getStringValue(): String {
    if (this.toInt().compareTo(this) == 0) {
        return this.toInt().toString()
    }
    return String.format("%.1f", this)
}