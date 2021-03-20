package br.com.zup.service

import io.micronaut.http.client.annotation.Client

@Client("http://localhost:50051")
interface CustomerClient {

//    @Get("/api/v1/clientes/{id}")
//    fun findBy
}