package emented.configuration

import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.ssl.SSLContextBuilder
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.ServerConnector
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
@LoadBalancerClient(name = "organization-service", configuration = [LoadBalancerConfiguration::class])
class ClientConfiguration {

    @Bean(name = ["mainRestTemplate"])
    @LoadBalanced
    fun mainRestTemplate(
        @Value("\${trust.store.path}") trustStore: Resource,
        @Value("\${trust.store.password}") trustStorePassword: String
    ): RestTemplate {
        val sslContext = SSLContextBuilder()
            .loadTrustMaterial(trustStore.url, trustStorePassword.toCharArray())
            .build()

        val sslConFactory = SSLConnectionSocketFactory(sslContext)

        val cm  = PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(sslConFactory)
            .build()

        val httpClient = HttpClients.custom().setConnectionManager(cm).build()
        val requestFactory  = HttpComponentsClientHttpRequestFactory(httpClient)

        return RestTemplate(requestFactory)
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