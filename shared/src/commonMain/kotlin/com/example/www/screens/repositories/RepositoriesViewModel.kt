package com.example.www.screens.repositories

import androidx.paging.PagingData
import com.example.www.domain.model.Repository
import com.example.www.domain.useCase.SearchRepositoriesUseCase
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class RepositoriesViewModel(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    ) : ViewModel() {

    private val _repositories: MutableStateFlow<PagingData<Repository>> = MutableStateFlow(value = PagingData.empty())
    val repositories: StateFlow<PagingData<Repository>> get() = _repositories

    fun onSearch(newQuery: String) {
        viewModelScope.launch {
            searchRepositoriesUseCase
                .invoke(newQuery)
                .distinctUntilChanged()
                .collect {
                    _repositories.value = it
                }
        }
    }
}
