package com.aljon.purrito.view

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.let {
        navigate(direction)
    }
}

fun NavController.safeNavigate(direction: NavDirections, extras: FragmentNavigator.Extras) {
    currentDestination?.getAction(direction.actionId)?.let {
        navigate(direction, extras)
    }
}