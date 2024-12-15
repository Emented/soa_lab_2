package emented.configuration

import com.netflix.client.config.DefaultClientConfigImpl

class RibbonClientConfig : DefaultClientConfigImpl() {

    override fun getDefaultSeverListClass(): String {
        return ConsulServerList::class.java.name
    }
}