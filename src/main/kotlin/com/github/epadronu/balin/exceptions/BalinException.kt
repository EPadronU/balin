/******************************************************************************
 * Copyright 2016 Edinson E. Padrón Urdaneta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

/* ***************************************************************************/
package com.github.epadronu.balin.exceptions
/* ***************************************************************************/

/* ***************************************************************************/
/**
 * The class `BalinException` and its subclasses indicate that something has
 * gone wrong with Balin.
 *
 * `RuntimeException` was chosen as the base-class in order to avoid
 * `BalinException` and its subclasses to be checked exceptions in Java.
 */
open class BalinException : RuntimeException {

    /**
     * Constructs a new exception with null as its detail message.
     */
    constructor() : super()

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message.
     */
    constructor(message: String) : super(message)

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    constructor(message: String, cause: Throwable?) : super(message, cause)

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * (`cause?.toString()`) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    constructor(cause: Throwable?) : super(cause)

    /**
     * Constructs a new exception with the specified detail message, cause, suppression
     * enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message.
     * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression whether or not suppression is enabled or disabled.
     * @param writableStackTrace whether or not the stack trace should be writable.
     */
    constructor(message: String, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )
}
/* ***************************************************************************/
