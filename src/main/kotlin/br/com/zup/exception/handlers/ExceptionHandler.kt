package br.com.zup.exception.handlers

interface ExceptionHandler<in E: Exception> {

    fun handle(exception: E): StatusWithDetails
    fun supports(exception: Exception): Boolean
}