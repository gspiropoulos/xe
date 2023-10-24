package com.example.xe.data.remote

import com.example.xe.domain.model.ResultItem

data class ResultsDtoItem(
    val mainText: String,
    val placeId: String,
    val secondaryText: String
)

fun ResultsDtoItem.toResultItem(): ResultItem {
    return ResultItem(
        id = placeId,
        placeName = mainText,
        region = secondaryText
    )
}