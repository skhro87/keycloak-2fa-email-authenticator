package skhro87.keycloak.authenticator.gateway

import skhro87.keycloak.authenticator.config.EmailAuthenticatorConfig
import skhro87.keycloak.authenticator.gateway.impl.KeycloakDefaultEmailGateway
import skhro87.keycloak.authenticator.gateway.impl.SimulationEmailGateway

class EmailGatewayFactory {
    fun gateway(config: EmailAuthenticatorConfig): IEmailGateway {
        return if (config.simulation) {
            SimulationEmailGateway()
        } else {
            KeycloakDefaultEmailGateway()
        }
    }
}
