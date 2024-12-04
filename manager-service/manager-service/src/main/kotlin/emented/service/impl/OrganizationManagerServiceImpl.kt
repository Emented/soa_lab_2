package emented.service.impl

import emented.api.NotFoundException
import emented.model.client.EmployeeRequest
import emented.model.client.EmployeeResponse
import emented.model.client.Organization
import emented.model.domain.Employee
import emented.service.OrganizationManagerService
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OrganizationManagerServiceImpl(
    private val restTemplate: RestTemplate,
) : OrganizationManagerService {
    override fun fireAllEmployees(organizationId: Long) {
        val response = restTemplate.getForEntity<Organization>(
            GET_ORGANIZATION_BY_ID_URL.format(organizationId),
            Organization::class.java
        )

        if (response.statusCode.value() == 404) {
            throw NotFoundException("Organization with id $organizationId not found")
        }

        if (response.statusCode.isError) {
            throw RuntimeException("Failed to get organization with id $organizationId")
        }

        var organization = response.body ?: throw NotFoundException("Organization with id $organizationId not found")

        organization.employees.forEach {
            fireEmployee(requireNotNull(it.id))
        }
    }

    override fun hireEmployee(organizationId: Long, employee: Employee) {
        val employeeRequest = EmployeeRequest(employee.name, organizationId)
        val response = restTemplate.postForEntity<EmployeeResponse>(
            HIRE_EMPLOYEE_URL,
            employeeRequest,
            EmployeeResponse::class.java
        )

        if (response.statusCode.isError) {
            throw RuntimeException("Failed to hire employee $employee")
        }
    }

    private fun fireEmployee(employeeId: Long) {
        restTemplate.delete(FIRE_EMPLOYEE_URL.format(employeeId))
    }

    companion object {
        private const val ORGANIZATION_SERVICE_URL = "https://organization-service:8181/organization-service"
        private const val GET_ORGANIZATION_BY_ID_URL = "$ORGANIZATION_SERVICE_URL/organizations/%d"
        private const val FIRE_EMPLOYEE_URL = "$ORGANIZATION_SERVICE_URL/employee/%d"
        private const val HIRE_EMPLOYEE_URL = "$ORGANIZATION_SERVICE_URL/employee"
    }
}