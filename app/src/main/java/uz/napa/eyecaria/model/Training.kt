package uz.napa.eyecaria.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Training(
    val thumbnail:Int,
    val description:String,
    val videoUrl:String
):Parcelable