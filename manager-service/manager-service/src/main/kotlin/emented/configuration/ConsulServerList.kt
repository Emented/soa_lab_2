package emented.configuration

import com.netflix.loadbalancer.Server
import com.netflix.loadbalancer.ServerList

class ConsulServerList : ServerList<Server> {

    private val consulRetriever = ConsulRetriever()

    override fun getInitialListOfServers(): List<Server> {
        return prepareServers()
    }

    override fun getUpdatedListOfServers(): List<Server> {
        return prepareServers()
    }

    private fun prepareServers(): List<Server> {
        return consulRetriever.getCrudAddresses().map { Server(it) }
    }
}