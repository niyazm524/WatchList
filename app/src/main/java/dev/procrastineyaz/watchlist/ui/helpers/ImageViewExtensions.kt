package dev.procrastineyaz.watchlist.ui.helpers

import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import dev.procrastineyaz.watchlist.R

fun ImageView.loadUserAvatar(avatarUrl: String?) {
    if(avatarUrl != null) {
        load(avatarUrl) {
            transformations(CircleCropTransformation())
        }
    } else {
        load(R.drawable.sharingan) {
            transformations(CircleCropTransformation())
        }
    }
}
