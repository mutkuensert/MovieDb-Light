package moviedblight.core.data

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface MyModelRepository {
    val myModels: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultMyModelRepository @Inject constructor() : MyModelRepository {

    override val myModels: Flow<List<String>> = flowOf(listOf())

    override suspend fun add(name: String) {}
}
