package core.domain.apikey

sealed interface ApiKeyState {
    class NotRequested : ApiKeyState
    class Success(val key: String) : ApiKeyState
    class Failed : ApiKeyState
}
