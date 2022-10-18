package skhro87.keycloak.authenticator.gateway.impl

import skhro87.keycloak.authenticator.gateway.IEmailGateway
import skhro87.keycloak.authenticator.gateway.dto.SendEmailRequest
import org.keycloak.email.DefaultEmailSenderProvider

class KeycloakDefaultEmailGateway : IEmailGateway {
    override fun send(req: SendEmailRequest) {
        val senderProvider = DefaultEmailSenderProvider(req.session)
        senderProvider.send(
            req.session.context.realm.smtpConfig,
            req.user,
            req.subject,
            req.textBody,
            req.textHtml
        )
    }
}
