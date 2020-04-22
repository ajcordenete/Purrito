package com.aljon.purrito.view

import android.app.AlertDialog
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.aljon.purrito.Event
import com.aljon.purrito.R
import com.google.android.material.snackbar.Snackbar

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<String>>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let {
            showSnackbar(it, timeLength)
        }
    })
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}

fun View.setUpAlertDialog(lifecycleOwner: LifecycleOwner,
                          dialogLiveEvent: LiveData<Event<Int>>,
                          alertMessage: Int,
                          actionPositive: () -> Unit,
                          actionPositiveText: Int,
                          actionNegativeText: Int = R.string.cancel_label,
                          actionNegative: () -> Unit = {}) {

    dialogLiveEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let { title ->
            showAlertDialog(title, alertMessage, actionPositiveText, actionPositive, actionNegativeText, actionNegative)
        }
    })
}

fun View.showAlertDialog(dialogTitle: Int,
                         dialogText: Int,
                         actionPositiveText: Int,
                         actionPositive: () -> Unit,
                         actionNegativeText: Int = R.string.cancel_label,
                         actionNegative: () -> Unit = {}) {
    AlertDialog.Builder(this.context)
        .setTitle(dialogTitle)
        .setMessage(dialogText)
        .setCancelable(true)
        .setNegativeButton(R.string.cancel_label, null)
        .setPositiveButton(actionPositiveText) { _, _ ->
            actionPositive()
        }
        .setNegativeButton(actionNegativeText) { _, _ ->
            actionNegative()
        }
        .create()
        .show()
}

fun Fragment.waitForTransition(targetView: View) {
    postponeEnterTransition()
    targetView.doOnPreDraw { startPostponedEnterTransition() }
}