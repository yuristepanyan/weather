package com.example.weather.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.example.weather.R

fun ViewGroup.inflate(@LayoutRes id: Int): View = LayoutInflater.from(this.context)
    .inflate(id, this, false)