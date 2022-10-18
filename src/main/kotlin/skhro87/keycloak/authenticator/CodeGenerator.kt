package skhro87.keycloak.authenticator

import org.keycloak.common.util.SecretGenerator

class CodeGenerator {
    private val secretGenerator = SecretGenerator.getInstance()

    fun code(length: Int): String {
        return secretGenerator.randomString(length)
    }
}
