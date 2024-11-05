package com.example.www.ui.screens.repositories

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.www.data.util.Result
import com.example.www.domain.model.Repository
import com.example.www.domain.useCase.SearchRepositoriesUseCase
import com.example.www.domain.useCase.StarRepositoryUseCase
import com.example.www.domain.useCase.UnStarRepositoryUseCase
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class RepositoriesViewModel(
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
    private val starRepositoryUseCase: StarRepositoryUseCase,
    private val unstarRepositoryUseCase: UnStarRepositoryUseCase
) : ViewModel() {

    private val _toastEvents = MutableSharedFlow<UIErrorType>()
    val toastEvents: SharedFlow<UIErrorType> = _toastEvents.asSharedFlow()

    private val _repositories: MutableStateFlow<PagingData<Repository>> = MutableStateFlow(value = PagingData.empty())
    val repositories: StateFlow<PagingData<Repository>> get() = _repositories

    fun onSearch(newQuery: String) {
        viewModelScope.launch {
            searchRepositoriesUseCase(newQuery)
                .distinctUntilChanged()
                .cachedIn(this)
                .collect {
                    _repositories.value = it
                }
        }
    }

    fun starUnstar(id: String, starUnstar: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val result: Result<Boolean> = if (starUnstar) {
                starRepositoryUseCase(id)
            } else {
                unstarRepositoryUseCase(id)
            }

            if (result is Result.Success) {
                _repositories.value = _repositories.value.map { repo ->
                    if (repo.nodeId == id) {
                        repo.let {
                            it.copy(isStarred = starUnstar, starsCount = (it.starsCount ?: 0) + if (starUnstar) 1 else -1)
                        }
                    } else repo
                }
            } else {
                //TODO-translations
                _toastEvents.emit(UIErrorType.TOAST("Something went wrong when trying to ${if (starUnstar) "star" else "unstar"} the repository"))
            }
        }
    }
}
