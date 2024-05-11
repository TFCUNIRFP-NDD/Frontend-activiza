package com.activiza.activiza.data

import com.google.gson.annotations.SerializedName

data class PexelsResponse (
    @SerializedName("photos") var photos:List<Photo>
)
data class Photo(
    @SerializedName("src") var src:Src
)
data class Src(
    @SerializedName("original") var original:String
)