package skhro87.keycloak.authenticator.gateway.dto

import org.keycloak.models.KeycloakSession
import org.keycloak.models.UserModel

data class SendEmailRequest(
    val session: KeycloakSession,
    val user: UserModel,
    val subject: String,
    val textBody: String,
    val textHtml: String
)
