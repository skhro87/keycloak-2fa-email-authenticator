package skhro87.keycloak.authenticator.config

class EmailAuthenticatorConfigParser {
    fun parseConfig(config: Map<String, String>): EmailAuthenticatorConfig {
        return EmailAuthenticatorConfig(
            emailSubject = config["emailSubject"] ?: "2FA Code",
            ttlSeconds = config["ttl"]?.toInt()
                ?: throw Exception("missing ttl config value"),
            simulation = config["simulation"]?.toBoolean() ?: false,
            length = config["length"]?.toInt() ?: 6,
        )
    }
}
