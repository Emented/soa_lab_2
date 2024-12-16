package emented.configuration

import com.orbitz.consul.Consul

class ConsulRetriever {

    fun getCrudAddresses(): List<Pair<String, Int>> {
        val client = Consul.builder().withUrl("http://consul:8500").build()
        val agentClient = client.agentClient()

        val services = agentClient.services
        val result = services.values
            .filter { it.service == "organization-service" }
            .map { it.address to it.port }

        println("Result: $result")
        return result
    }

}