package com.samuel.finances.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.finances.repository.CategoryRepository
import com.samuel.finances.repository.MockCategoryRepository
import com.samuel.finances.ui.theme.CategoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository : CategoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoryUiState>(
        CategoryUiState.Loading
    )

    val uiState: StateFlow<CategoryUiState> = _uiState

    init { loadCategories() }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = CategoryUiState.Loading

            try {
                val categories = repository.getCategories()

                _uiState.value = CategoryUiState.Success(categories)
            } catch (e: Exception) {
                _uiState.value = CategoryUiState.Error(
                    e.message ?: "Unable to load categories."
                )
            }
        }
    }
}