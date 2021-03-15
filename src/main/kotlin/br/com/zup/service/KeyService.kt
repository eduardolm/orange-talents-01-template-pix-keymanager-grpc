package br.com.zup.service

import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.response.CreatePixKeyResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8082")
interface KeyService {

    @Post( "/api/v1/pix/keys", produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun create(@Body createPixKeyRequest: CreatePixKeyRequest): CreatePixKeyResponse
}