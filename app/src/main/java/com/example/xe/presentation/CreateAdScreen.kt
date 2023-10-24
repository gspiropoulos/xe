package com.example.xe.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.xe.common.Constants
import com.google.gson.Gson

@Composable
fun CreateAdScreen(viewModel: CreateAdViewModel = hiltViewModel()) {

    var submitEnabled by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var location by remember { mutableStateOf("") }
    val jsonToShow = remember { mutableStateOf("") }
    var isDialogVisible by remember { mutableStateOf(false) }

    val priceFocusRequester = remember { FocusRequester() }
    val locationFocusRequester = remember {
        FocusRequester()
    }
    val descriptionFocusRequester = remember {
        FocusRequester()
    }
    var clickedLocation by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .weight(2f)
        )
        {
            item {
                Text(
                    text = Constants.screenHeaderText,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

            }
            item {
                // Title
                Text(
                    text = Constants.titleHeader,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = uiState.userInput.title,
                    onValueChange = {
                        viewModel.updateTitle(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { locationFocusRequester.requestFocus() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .onFocusEvent {
                            if (it.isFocused) {
                                clickedLocation = true
                            }
                        },
                    placeholder = {
                        Text(Constants.hintText)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                // Location
                Text(
                    text = Constants.locationHeader,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = uiState.locationPrefix,
                    onValueChange = {
                        viewModel.onSearchTextChange(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { priceFocusRequester.requestFocus() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .focusRequester(locationFocusRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                clickedLocation = false
                            }
                        },
                    placeholder = {
                        Text(Constants.hintText)
                    }
                )
            }
            if (uiState.locations.isEmpty() || clickedLocation || uiState.userInput.error.isNotEmpty()) {
                //do not show results
            } else {
                items(uiState.locations) { result ->
                    Text(
                        text = "${result.placeName} ${result.region}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clickable {
                                viewModel.onSearchTextChange("${result.placeName} ${result.region}")
                                location = "${result.placeName} ${result.region}"
                                focusManager.clearFocus()
                                clickedLocation = true
                            }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Price
                Text(
                    text = Constants.priceHeader,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = uiState.userInput.price,
                    onValueChange = {
                        viewModel.updatePrice(it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { descriptionFocusRequester.requestFocus() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .focusRequester(priceFocusRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                clickedLocation = true
                            }
                        },
                    placeholder = {
                        Text(Constants.hintText)
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = Constants.descriptionHeader,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = uiState.userInput.description,
                    onValueChange = {
                        viewModel.updateDescription(it)
                        clickedLocation = true
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .focusRequester(descriptionFocusRequester)
                        .onFocusEvent {
                            if (it.isFocused) {
                                clickedLocation = true
                            }
                        },
                    placeholder = {
                        Text(Constants.hintText)
                    }
                )

            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,

                ) {
                Button(
                    onClick = {
                        jsonToShow.value =
                            Gson().toJson("Title:${uiState.userInput.title} Location:${uiState.userInput.location} Price:${uiState.userInput.price} Description:${uiState.userInput.description}")
                        isDialogVisible = true
                    },
                    enabled = submitEnabled
                ) {
                    Text(Constants.submit)
                }

                Button(
                    onClick = {
                        viewModel.clearInputs()
                        submitEnabled = false
                        viewModel.clearSearch()
                        viewModel.onSearchTextChange("")
                    }
                ) {
                    Text(Constants.clear)
                }
            }
        }
    }
    if (isDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                isDialogVisible = false
            },
            title = { Text(Constants.submittedInfo) },
            text = { Text(jsonToShow.value) },
            confirmButton = {
                Button(onClick = {
                    isDialogVisible = false
                    viewModel.clearInputs()
                    submitEnabled = false
                    viewModel.clearSearch()
                    viewModel.onSearchTextChange("")
                }) {
                    Text(Constants.okText)
                }
            }
        )
    }
    submitEnabled = uiState.userInput.title.isNotEmpty() && location.isNotEmpty()

}




