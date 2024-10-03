package com.example.www.screens.repositories

import com.example.www.data.GitHubRepository
import com.example.www.domain.model.Repositories
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

class RepositoriesViewModel(gitHubRepository: GitHubRepository) : ViewModel() {
    @NativeCoroutinesState
    val repositories: StateFlow<Repositories> =
        gitHubRepository.getRepositories()
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), Repositories(0, false, null)
            )
}
