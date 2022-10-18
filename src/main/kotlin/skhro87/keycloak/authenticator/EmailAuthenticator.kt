package skhro87.keycloak.authenticator

import skhro87.keycloak.authenticator.config.EmailAuthenticatorConfigParser
import skhro87.keycloak.authenticator.gateway.EmailGatewayFactory
import skhro87.keycloak.authenticator.gateway.dto.SendEmailRequest
import org.keycloak.authentication.AuthenticationFlowContext
import org.keycloak.authentication.AuthenticationFlowError
import org.keycloak.authentication.Authenticator
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.sessions.AuthenticationSessionModel
import org.keycloak.theme.Theme
import javax.ws.rs.core.Response

class EmailAuthenticator : Authenticator {
    private val configParser = EmailAuthenticatorConfigParser()
    private val codeGenerator = CodeGenerator()

    override fun authenticate(context: AuthenticationFlowContext) {
        // load from context
        val configModel = context.authenticatorConfig
        val session = context.session
        val user = context.user
        val config = configParser.parseConfig(configModel.config)

        // generate code and set into session
        val code = codeGenerator.code(config.length)
        val authSession: AuthenticationSessionModel = context.authenticationSession
        authSession.setAuthNote("code", code)
        authSession.setAuthNote(
            "ttl",
            (System.currentTimeMillis() + config.ttlSeconds * 1000).toString()
        )

        // load theme and send email
        try {
            val theme = session.theme().getTheme(Theme.Type.LOGIN)
            val locale = session.context.resolveLocale(user)
            val authText = theme.getMessages(locale).getProperty("emailAuthText")
            val text = String.format(authText, code, Math.floorDiv(config.ttlSeconds, 60))

            EmailGatewayFactory().gateway(config).send(
                SendEmailRequest(
                    session = session,
                    user = user,
                    subject = config.emailSubject,
                    textBody = text,
                    textHtml = text
                )
            )
            context.challenge(
                context.form()
                    .setAttribute("realm", context.realm)
                    .createForm(TPL_CODE)
            )
        } catch (e: Exception) {
            context.failureChallenge(
                AuthenticationFlowError.INTERNAL_ERROR,
                context.form().setError("emailAuthEmailNotSent", e.message)
                    .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR)
            )
        }
    }

    override fun action(context: AuthenticationFlowContext) {
        // parse data from context and session
        val enteredCode = context.httpRequest.decodedFormParameters.getFirst("code")
        val authSession: AuthenticationSessionModel = context.authenticationSession
        val code = authSession.getAuthNote("code")
        val ttl = authSession.getAuthNote("ttl")

        // validate input
        if (code.isNullOrBlank() || ttl == null) {
            context.failureChallenge(
                AuthenticationFlowError.INTERNAL_ERROR,
                context.form()
                    .createErrorPage(Response.Status.INTERNAL_SERVER_ERROR)
            )
            return
        }

        // check if code is valid
        val isValid = enteredCode == code
        if (isValid) {
            // check if code is expired
            if (ttl.toLong() < System.currentTimeMillis()) {
                context.failureChallenge(
                    AuthenticationFlowError.EXPIRED_CODE,
                    context.form()
                        .setError("emailAuthCodeExpired")
                        .createErrorPage(Response.Status.BAD_REQUEST)
                )
            } else {
                // valid
                context.success()
            }
        } else {
            // invalid
            val execution = context.execution
            if (execution.isRequired) {
                context.failureChallenge(
                    AuthenticationFlowError.INVALID_CREDENTIALS,
                    context.form()
                        .setAttribute("realm", context.realm)
                        .setError("emailAuthCodeInvalid")
                        .createForm(TPL_CODE)
                )
            } else if (execution.isConditional || execution.isAlternative) {
                context.attempted()
            }
        }
    }

    override fun requiresUser(): Boolean {
        return true
    }

    override fun configuredFor(session: KeycloakSession?, realm: RealmModel?, user: UserModel): Boolean {
        return user.email != null
    }

    override fun setRequiredActions(session: KeycloakSession?, realm: RealmModel?, user: UserModel?) {}

    override fun close() {}

    companion object {
        private const val TPL_CODE = "login-email.ftl"
    }
}
