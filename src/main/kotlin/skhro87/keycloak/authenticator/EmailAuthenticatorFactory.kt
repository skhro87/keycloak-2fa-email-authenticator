package skhro87.keycloak.authenticator

import org.keycloak.Config
import org.keycloak.authentication.Authenticator
import org.keycloak.authentication.AuthenticatorFactory
import org.keycloak.models.AuthenticationExecutionModel.Requirement
import org.keycloak.models.KeycloakSession
import org.keycloak.models.KeycloakSessionFactory
import org.keycloak.provider.ProviderConfigProperty
import org.keycloak.provider.ServerInfoAwareProviderFactory

class EmailAuthenticatorFactory : AuthenticatorFactory, ServerInfoAwareProviderFactory {
    override fun getId(): String {
        return "email-authenticator"
    }

    override fun getDisplayType(): String {
        return "Email Authentication"
    }

    override fun getHelpText(): String {
        return "Validates an OTP sent via Email."
    }

    override fun getReferenceCategory(): String {
        return "otp"
    }

    override fun isConfigurable(): Boolean {
        return true
    }

    override fun isUserSetupAllowed(): Boolean {
        return false
    }

    override fun getRequirementChoices(): Array<Requirement> {
        return arrayOf(
            Requirement.REQUIRED,
            Requirement.ALTERNATIVE,
            Requirement.DISABLED
        )
    }

    override fun getConfigProperties(): List<ProviderConfigProperty> {
        return listOf(
            ProviderConfigProperty(
                "emailSubject",
                "Email Subject",
                "The subject of the email being sent.",
                ProviderConfigProperty.STRING_TYPE,
                "2FA Code"
            ),
            ProviderConfigProperty(
                "ttl",
                "Time-to-live",
                "The time to live in seconds for the code to be valid.",
                ProviderConfigProperty.STRING_TYPE,
                "300"
            ),
            ProviderConfigProperty(
                "simulation",
                "Simulation mode",
                "In simulation mode, the Email won't be sent, but printed to the server logs",
                ProviderConfigProperty.BOOLEAN_TYPE,
                true
            ),
            ProviderConfigProperty(
                "length",
                "Code length",
                "The number of digits of the generated code.",
                ProviderConfigProperty.STRING_TYPE,
                "6"
            ),
        )
    }

    override fun create(session: KeycloakSession): Authenticator {
        return EmailAuthenticator()
    }

    override fun init(config: Config.Scope) {}
    override fun postInit(factory: KeycloakSessionFactory) {}
    override fun close() {}
    override fun getOperationalInfo(): Map<String, String> {
        return mapOf(Pair("Version", "demo"))
    }
}
