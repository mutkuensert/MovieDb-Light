package moviedblight.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import moviedblight.core.data.MyModelRepository

class MyModelViewModel(
    private val myModelRepository: MyModelRepository
) : ViewModel() {

    val uiState: StateFlow<MyModelUiState> = myModelRepository
        .myModels.map<List<String>, MyModelUiState> { MyModelUiState.Success(data = it) }
        .catch { emit(MyModelUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MyModelUiState.Loading)

    fun addMyModel(name: String) {
        viewModelScope.launch {
            myModelRepository.add(name)
        }
    }
}

sealed interface MyModelUiState {
    object Loading : MyModelUiState
    data class Error(val throwable: Throwable) : MyModelUiState
    data class Success(val data: List<String>) : MyModelUiState
}
