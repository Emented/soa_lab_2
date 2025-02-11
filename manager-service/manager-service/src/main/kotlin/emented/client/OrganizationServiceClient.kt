package emented.client

import emented.model.client.EmployeeRequest
import emented.model.client.EmployeeResponse
import emented.model.client.Organization
import emented.model.domain.Employee
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException.NotFound
import org.springframework.web.client.RestTemplate

@Component
class OrganizationServiceClient(
    @Qualifier("mainRestTemplate") private val restTemplate: RestTemplate,
) {

    fun getOrganization(organizationId: Long): Organization? {
        return try {
            val response = restTemplate.getForEntity<Organization>(
                GET_ORGANIZATION_BY_ID_URL.format(organizationId),
                Organization::class.java
            )

            if (response.statusCode.value() == 404) {
                return null
            }

            if (response.statusCode.isError) {
                throw RuntimeException("Failed to get organization with id $organizationId")
            }

            return response.body
        } catch (_: NotFound) {
            return null
        }

        return null
    }

    fun hireEmployee(organizationId: Long, employee: Employee) {
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

    fun fireEmployee(employeeId: Long) {
        restTemplate.delete(FIRE_EMPLOYEE_URL.format(employeeId))
    }

    companion object {
        private const val ORGANIZATION_SERVICE_URL = "https://organization-service"
        private const val GET_ORGANIZATION_BY_ID_URL = "$ORGANIZATION_SERVICE_URL/organizations/%d"
        private const val FIRE_EMPLOYEE_URL = "$ORGANIZATION_SERVICE_URL/employee/%d"
        private const val HIRE_EMPLOYEE_URL = "$ORGANIZATION_SERVICE_URL/employee"
    }
}