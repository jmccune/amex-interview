package com.amex.interview.annotations

// I feel like I need to go back and forth on this.
// For a time it didn't seem necessary for an entity to have the NoArgConstructor.
// Not sure if this is related to the switch to Jakarta, that this is a spring beta
// or some missing configuration or something else...
//
// See: https://kotlinlang.org/docs/no-arg-plugin.html#gradle
// Errors encountered were:
//  No Default Constructor  (when hibernate was trying to create a new Kotlin entity)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class NoArgConstructor()
