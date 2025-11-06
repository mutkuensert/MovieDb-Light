package libraries

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class AppScope() : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.IO
}