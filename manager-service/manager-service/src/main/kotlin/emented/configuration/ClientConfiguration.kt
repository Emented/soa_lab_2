package emented.configuration

import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.ServerConnector
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.http.HttpClient
import java.time.Duration
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLParameters

@Configuration
class ClientConfiguration {

    @Bean
    fun organizationServiceHttpClient(): HttpClient {
        return HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .sslContext(SSLContext.getDefault())
            .connectTimeout(Duration.ofMillis(1000))
            .build()
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