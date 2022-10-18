package skhro87.keycloak.authenticator.gateway.impl

import skhro87.keycloak.authenticator.gateway.IEmailGateway
import skhro87.keycloak.authenticator.gateway.dto.SendEmailRequest
import org.jboss.logging.Logger

class SimulationEmailGateway : IEmailGateway {
    private val log = Logger.getLogger(SimulationEmailGateway::class.java)

    override fun send(req: SendEmailRequest) {
        log.debug(
            "[EMAIL 2FA SIMULATION] sending email to ${req.user.email} with text ${req.textBody}"
        )
    }
}
