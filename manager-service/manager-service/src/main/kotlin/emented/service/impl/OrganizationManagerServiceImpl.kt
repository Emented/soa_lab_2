package emented.service.impl

import emented.client.OrganizationServiceClient
import emented.model.domain.Employee
import emented.service.OrganizationManagerService
import org.springframework.stereotype.Service

@Service
class OrganizationManagerServiceImpl(
    private val organizationServiceClient: OrganizationServiceClient
) : OrganizationManagerService {
    override fun fireAllEmployees(organizationId: Long) {
        val organization = organizationServiceClient.getOrganization(organizationId)

        organization.employees.forEach {
            organizationServiceClient.fireEmployee(requireNotNull(it.id))
        }
    }

    override fun hireEmployee(organizationId: Long, employee: Employee) {
        organizationServiceClient.hireEmployee(organizationId, employee)
    }
}