package com.example.xe.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xe.common.Resource
import com.example.xe.domain.model.ResultItem
import com.example.xe.domain.model.UiState
import com.example.xe.domain.model.UserInput
import com.example.xe.domain.usecase.GetLocationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class CreateAdViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    private val _locations = MutableStateFlow<List<ResultItem>>(emptyList())
    private val _userInput = MutableStateFlow(UserInput())

    val uiState =
        combine(_searchText, _locations, _userInput) { searchText, cities, userInput ->

            if (searchText.isBlank())
                clearSearch()

            UiState(
                searchText,
                cities,
                userInput
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState()
        )

    init {
        _searchText
            .debounce(500L)
            .filter { (it.length > 2) }
            .flowOn(Dispatchers.IO)
            .onEach { cityPrefix ->
                getLocations(cityPrefix)
            }
            .launchIn(viewModelScope)
        _userInput.debounce(0).flowOn(Dispatchers.IO).launchIn(viewModelScope)
    }

    private fun getLocations(prefix: String) {

        getLocationsUseCase(prefix).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { _locations.value = it }
                    Log.d("", "${result.data}")
                }

                is Resource.Error -> {
                    result.message?.let { _userInput.value = _userInput.value.copy(error = it) }

                }
            }
        }.launchIn(viewModelScope)


    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        _userInput.value = _userInput.value.copy(location = text)
    }

    fun updateTitle(title: String) {
        _userInput.value = _userInput.value.copy(title = title)

    }

    fun updatePrice(price: String) {
        _userInput.value = _userInput.value.copy(price = price)
    }

    fun updateDescription(description: String) {
        _userInput.value = _userInput.value.copy(description = description)
    }

    fun clearSearch() {
        _locations.value = emptyList()
    }

    fun clearInputs() {
        _userInput.value =
            _userInput.value.copy(title = "", location = "", price = "", description = "")
    }
}