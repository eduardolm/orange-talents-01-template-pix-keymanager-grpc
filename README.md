# Pix KeyManager - gRPC
![Version](src/main/resources/static/img/version.svg)
![License](src/main/resources/static/img/license.svg)
![Coverage](src/main/resources/static/img/coverage.svg)
## Contexto

Desenvolver um microserviço gRPC que simule a gestão de chaves Pix. Este sistema deve permitir cadastrar, listar, consultar e excluir chaves Pix. Além das funcionalidades mencionadas, o sistema também deve ser responsável por toda a comunicação com o Banco Central para gerenciamento das chaves Pix.
Os dados de chaves gerenciados pelo sistema devem ser persistidos para melhorar a disponibilidade do serviço e agilizar consultas.
Este serviço irá se comunicar com outro serviço REST, responsável por expor as suas funcionalidades ao frontend.

## Tecnologias utilizadas
O projeto foi desenvolvido utilizando as seguintes tecnologias:

 + Kotlin 1.4.32
 + Micronaut 2.4.1
 + Docker
 + Docker-compose
 + MySQL
 + Github

## Endpoints & Payloads
### Endpoints
#### Chave Pix
**Ação** | **Endpoint gRPC** | **Método equivalente HTTP**
---------- | ------------ | ----------
Criar chave Pix | _/api/v1/pix/keys_ | POST
Listar por id | _/api/v1/pix/keys/{id}_ | GET
Listar todas | _/api/v1/pix/keys/all/{id}_ | GET
Listar todas | _/api/v1/pix/keys_ | DELETE

### Payloads - (request & response)
#### Cadastrar Chave Pix
##### POST - request
    {
        "keyType": "PHONE",
        "key": "+554733256478",
        "bankAccount": {
            "branch": "0001",
            "accountNumber": "123654",
            "accountType": "CONTA_CORRENTE",
            "institution": {
                "name": "ITAÚ UNIBANCO S.A.",
                "participant": "60701190"
                }
        },
        "owner": {
            "id": "ecc0b904-71f3-40b8-80a7-4a5b6f4f753f",
            "name": "John Doe",
            "taxIdNumber": "74125896321"
        }
    }

##### POST - response
    {
        "clientId": "ecc0b904-71f3-40b8-80a7-4a5b6f4f753f",
        "pixId": "28863eea-7e78-43df-afb4-972678fd61d7",
        "createdAt": "2021-03-31T13:57:44.849501"
    }

##### GET - Listar por id (request)
    {
        "pixKey": "28863eea-7e78-43df-afb4-972678fd61d7",
        "clientId": "ecc0b904-71f3-40b8-80a7-4a5b6f4f753f"
    }

##### GET - Listar por id (response)
    {
        "pixId": "28863eea-7e78-43df-afb4-972678fd61d7",
        "clientId": "ecc0b904-71f3-40b8-80a7-4a5b6f4f753f",
        "keyType": "RANDOM",
        "pixKey": "e5b3497d-0323-4c09-a3c7-824d5bc8a3e4",
        "owner": {
            "name": "John Doe",
            "taxIdNumber": "74125896321"
        },
        "account": {
            "branch": "0001",
            "accountNumber": "123654",
            "accountType": "CONTA_POUPANCA",
            "institution": {
                "name": "ITAÚ UNIBANCO S.A.",
                "participant": "60701190"
            }
        },
        "createdAt": "2021-03-31T13:57:44.849501"
    }

##### GET - Listar todas por clientId (request)
    {
        "clientId": "ecc0b904-71f3-40b8-80a7-4a5b6f4f753f"
    }

##### GET - Listar todas por clientId (response)
    {
        "pixKeys": [
            {
                "pixId": "28863eea-7e78-43df-afb4-972678fd61d7",
                "keyType": "RANDOM",
                "pixKey": "e5b3497d-0323-4c09-a3c7-824d5bc8a3e4",
                "accountType": "CONTA_POUPANCA",
                "createdAt": "2021-03-31T13:57:44.849501"
            },
            {
                "pixId": "ae312145-d45f-4f21-963f-740b8cbbb0fe",
                "keyType": "PHONE",
                "pixKey": "+554733256478",
                "accountType": "CONTA_CORRENTE",
                "createdAt": "2021-03-30T18:57:24.459379"
            }
        ],
        "clientId": "ecc0b904-71f3-40b8-80a7-4a5b6f4f753f"
    }

##### DELETE - Excluir (request)
    {
        "pixId": "28863eea-7e78-43df-afb4-972678fd61d7",
        "clientId": "ecc0b904-71f3-40b8-80a7-4a5b6f4f753f"
    }

##### DELETE - Excluir (response)
    {
        "key": "e5b3497d-0323-4c09-a3c7-824d5bc8a3e4",
        "participant": "60701190",
        "deletedAt": "2021-03-31T14:09:01.477997"
    }

