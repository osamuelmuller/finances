package com.samuel.finances.ui.theme

import com.samuel.finances.model.Category

sealed interface CategoryUiState {

    data object Loading : CategoryUiState

    data class Success (
        val categories: List<Category>
    ) : CategoryUiState

    data class Error (
        val message: String
    ) : CategoryUiState

}