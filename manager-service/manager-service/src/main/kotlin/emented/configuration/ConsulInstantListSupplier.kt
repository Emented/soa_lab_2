package emented.configuration

import org.springframework.cloud.client.DefaultServiceInstance
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import reactor.core.publisher.Flux

class ConsulInstantListSupplier : ServiceInstanceListSupplier {

    private val name = "organization-service"

    private val consulRetriever = ConsulRetriever()

    override fun getServiceId(): String {
        return name
    }

    override fun get(): Flux<List<ServiceInstance>> {
        val services = consulRetriever.getCrudAddresses()
        return Flux.just(
            services.mapIndexed { idx, service ->
                DefaultServiceInstance("$name-$idx", name, service.first, service.second, true)
            }
        )
    }
}