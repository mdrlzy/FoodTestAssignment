package com.mdr.food.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdr.food.domain.Food
import com.mdr.food.domain.FoodRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesModel(val categories: List<String>, val selected: Int)

class MainViewModel(
    val foodRepo: FoodRepo
) : ViewModel() {

    private val _foodFlow = MutableStateFlow<List<Food>?>(null)
    val foodFlow: StateFlow<List<Food>?> = _foodFlow


    private val _categoriesFlow = MutableStateFlow<CategoriesModel?>(null)
    val categoriesFlow: StateFlow<CategoriesModel?> = _categoriesFlow

    init {
        viewModelScope.launch {
            val categoriesModel = CategoriesModel(foodRepo.getCategories(), 0)
            val foods =
                foodRepo.getFoodByCategory(categoriesModel.categories[categoriesModel.selected])
            _foodFlow.emit(foods)
            _categoriesFlow.emit(categoriesModel)
        }
    }
}