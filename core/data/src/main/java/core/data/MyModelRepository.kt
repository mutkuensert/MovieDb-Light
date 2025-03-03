package core.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MyModelRepository {
    val myModels: StateFlow<List<String>>

    suspend fun add(name: String)
}

class DefaultMyModelRepository : MyModelRepository {
    private val _myModels = MutableStateFlow(listOf<String>())
    override val myModels: StateFlow<List<String>> = _myModels.asStateFlow()

    override suspend fun add(name: String) {
        val list = myModels.value.toMutableList()
        list.add(name)
        _myModels.value = list
    }
}
