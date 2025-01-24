package emented.service.impl

import emented.client.OrganizationServiceClient
import emented.model.domain.Employee
import emented.service.OrganizationManagerService
import org.springframework.stereotype.Service

@Service
class OrganizationManagerServiceImpl(
    private val organizationServiceClient: OrganizationServiceClient
) : OrganizationManagerService {
    override fun fireAllEmployees(organizationId: Long): Boolean {
        val organization = organizationServiceClient.getOrganization(organizationId)

        if (organization == null) {
            return false
        }

        organization.employees.forEach {
            organizationServiceClient.fireEmployee(requireNotNull(it.id))
        }

        return true
    }

    override fun hireEmployee(organizationId: Long, employee: Employee) {
        organizationServiceClient.hireEmployee(organizationId, employee)
    }
}