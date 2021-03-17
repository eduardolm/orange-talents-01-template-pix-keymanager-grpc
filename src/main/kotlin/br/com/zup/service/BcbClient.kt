package br.com.zup.service

import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.request.RemoveKeyRequestDto
import br.com.zup.dto.response.CreatePixKeyResponse
import br.com.zup.dto.response.RemoveKeyResponseDto
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8082")
interface BcbClient {

    @Post( "/api/v1/pix/keys", produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun create(@Body createPixKeyRequest: CreatePixKeyRequest): CreatePixKeyResponse

    @Delete("/api/v1/pix/keys/{key}", produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun delete(@PathVariable("key") key: String,
               @Body request: RemoveKeyRequestDto): RemoveKeyResponseDto
}