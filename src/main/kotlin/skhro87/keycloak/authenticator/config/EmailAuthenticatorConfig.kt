package skhro87.keycloak.authenticator.config

data class EmailAuthenticatorConfig(
    val emailSubject: String,
    val ttlSeconds: Int,
    val simulation: Boolean,
    val length: Int,
)
