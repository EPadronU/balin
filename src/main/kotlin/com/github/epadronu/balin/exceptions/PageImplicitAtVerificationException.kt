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
 * This exception is thrown when the _implicit at verification_ of a page
 * doesn't pass after the browser has tried navigating to it.
 *
 * @sample com.github.epadronu.balin.core.PageTests.model_a_page_into_a_page_object_with_a_invalid_at_clause
 */
class PageImplicitAtVerificationException : BalinException("The browser is not located at the expected page")
/* ***************************************************************************/
