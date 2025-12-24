package core.domain

interface Failure {
    val message: String
}

class UndefinedFailure(override val message: String) : Failure
class AuthFailure(override val message: String) : Failure