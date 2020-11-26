package com.jama.mpesa_biz_no_detector.utils

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator

private fun options(popUpTo: Int, popUpToInclusive: Boolean) = NavOptions.Builder().apply {
    setPopUpTo(popUpTo, popUpToInclusive)
}.build()

fun NavController.navigateToFragment(
    destination: Int,
    bundle: Bundle? = null,
    extras: FragmentNavigator.Extras? = null,
    popUpTo: Int = -1,
    popUpToInclusive: Boolean = false
) {
    this.navigate(destination, bundle, options(popUpTo, popUpToInclusive), extras)
}