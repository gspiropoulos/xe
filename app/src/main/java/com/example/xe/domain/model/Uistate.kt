package com.example.xe.domain.model


data class UiState(
    val locationPrefix: String = "",
    val locations: List<ResultItem> = emptyList(),
    val userInput: UserInput = UserInput(),
)