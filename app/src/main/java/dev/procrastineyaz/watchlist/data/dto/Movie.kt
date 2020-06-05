package dev.procrastineyaz.watchlist.data.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Long,
    val title: String,
    val titleGlobal: String,
    val rating: Float,
    val note: String,
    val posterUri: String?
) : Parcelable
