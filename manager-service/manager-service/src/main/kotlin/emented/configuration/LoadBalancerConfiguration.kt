package emented.configuration

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoadBalancerConfiguration {

    @Bean
    fun serviceInstanceListSupplier(): ServiceInstanceListSupplier {
        return ConsulInstantListSupplier()
    }

}