package emented.configuration

import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.ServerConnector
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
class ApplicationConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.invoke {
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
            cors { disable() }
            csrf { disable() }
        }

        return http.build()
    }

    @Bean
    fun disableSniHostCheck(): WebServerFactoryCustomizer<JettyServletWebServerFactory> {
        return WebServerFactoryCustomizer<JettyServletWebServerFactory> { factory ->
            factory.addServerCustomizers(JettyServerCustomizer { server ->
                server.connectors.forEach {
                    if (it is ServerConnector) {
                        val serverConnector = it
                        val connectionFactory = serverConnector.getConnectionFactory(HttpConnectionFactory::class.java)
                        if (connectionFactory != null) {
                            val secureRequestCustomizer = connectionFactory.httpConfiguration
                                .getCustomizer(SecureRequestCustomizer::class.java)
                            if (secureRequestCustomizer != null) {
                                secureRequestCustomizer.isSniHostCheck = false
                            }
                        }
                    }
                }
            })
        }
    }

}