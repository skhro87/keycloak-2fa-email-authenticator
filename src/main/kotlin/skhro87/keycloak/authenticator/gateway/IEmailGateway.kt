package skhro87.keycloak.authenticator.gateway

import skhro87.keycloak.authenticator.gateway.dto.SendEmailRequest

interface IEmailGateway {
    fun send(req: SendEmailRequest)
}
