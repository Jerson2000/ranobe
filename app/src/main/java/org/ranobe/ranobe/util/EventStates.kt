package org.ranobe.ranobe.util

sealed class EventStates<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T):EventStates<T>(data)
    class Error<T>(message: String?):EventStates<T>(message=message)
    class Loading<T>():EventStates<T>()
    class Unspecified<T>():EventStates<T>()
}