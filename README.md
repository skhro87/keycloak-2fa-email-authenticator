# Keycloak 2FA Email Authentication Plugin

Keycloak 2FA Email Authentication Plugin to add Email-based 2FA support to Keycloak.
 
Plugin uses the default SMTP provider that you can set in `Realm Settings -> Email`. 

Tested with `Keycloak <= v18.0.0`.

__For demo purposes only. Use at your own risk.__

Inspired by [Keycloak 2FA SMS Authenticator](https://github.com/dasniko/keycloak-2fa-sms-authenticator)

## Setup

1. build JAR file

    ```
    ./gradlew shadowJar
    ```

    You can find the `.jar` in `build/libs`.

2. Copy files into Keycloak

   * `cp build/libs/keycloak-email-authenticator-1.0-SNAPSHOT-all.jar /opt/keycloak/providers/`
   * `cp src/main/resources/theme-resources/templates/login-email.ftl /opt/jboss/keycloak/themes/base/login/`

3. Add message texts e.g. like this

   * `cat src/main/resources/theme-resources/messages/messages_en.properties >> /opt/jboss/keycloak/themes/base/login/messages/messages_en.properties`

4. Setup new Authentication flow in Keycloak `Authentication -> Flows`

5. Optional: Configure Plugin Settings like `Email Subject`, `Code Length`, `TTL`, ...