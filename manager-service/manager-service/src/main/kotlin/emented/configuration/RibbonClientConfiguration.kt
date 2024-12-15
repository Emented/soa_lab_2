package emented.configuration

import com.netflix.client.config.CommonClientConfigKey
import com.netflix.client.config.DefaultClientConfigImpl.DEFAULT_CONNECT_TIMEOUT
import com.netflix.client.config.DefaultClientConfigImpl.DEFAULT_READ_TIMEOUT
import com.netflix.client.config.IClientConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.io.HttpClientConnectionManager
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.ssl.SSLContextBuilder
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.SecureRequestCustomizer
import org.eclipse.jetty.server.ServerConnector
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.ribbon.RibbonClientName
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.Resource
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import javax.net.ssl.SSLContext

@Configuration
class RibbonClientConfiguration {

    @RibbonClientName
    private val name = "organization-service"

    @Bean
    @Primary
    fun ribbonClientConfig(): IClientConfig  {
        var config = RibbonClientConfig()
        config.loadProperties(this.name)

        config.set(CommonClientConfigKey.ConnectTimeout, DEFAULT_CONNECT_TIMEOUT)
        config.set(CommonClientConfigKey.ReadTimeout, DEFAULT_READ_TIMEOUT)
        config.set(CommonClientConfigKey.GZipPayload, false)
        config.set(CommonClientConfigKey.ServerListRefreshInterval, 5000)
        config.set(CommonClientConfigKey.NIWSServerListClassName, ConsulServerList::class.java.name)

        return config
    }

    @Bean
    @LoadBalanced
    fun restTemplate(
        @Value("\${trust.store}") trustStore: Resource,
        @Value("\${trust.store.password}") trustStorePassword: String
    ): RestTemplate {
        val sslContext: SSLContext = SSLContextBuilder()
            .loadTrustMaterial(trustStore.url, trustStorePassword.toCharArray()).build()

        val sslConFactory = SSLConnectionSocketFactory(sslContext)

        val cm: HttpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setSSLSocketFactory(sslConFactory)
            .build()

        val httpClient: CloseableHttpClient = HttpClients.custom().setConnectionManager(cm).build()
        val requestFactory: ClientHttpRequestFactory = HttpComponentsClientHttpRequestFactory(httpClient)

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