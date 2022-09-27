package com.amex.interview.util

import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock

class TestHelper {
    companion object {
        fun <T> myAny(type: Class<T>, nonNull:T): T {
            ArgumentMatchers.any(type)
            //Cannot return NULL for Kotlin, so return NonNull "object"
            return nonNull
        }
    }
}