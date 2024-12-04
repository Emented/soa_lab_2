package emented.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import emented.api.NotFoundException
import emented.model.client.EmployeeRequest
import emented.model.client.Organization
import emented.model.domain.Employee
import emented.service.OrganizationManagerService
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class OrganizationManagerServiceImpl(
    private val organizationServiceHttpClient: HttpClient,
    private val objectMapper: ObjectMapper
) : OrganizationManagerService {
    override fun fireAllEmployees(organizationId: Long) {
        val request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(GET_ORGANIZATION_BY_ID_URL.format(organizationId)))
            .build()

        val response = organizationServiceHttpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response?.statusCode() == 404) {
            throw NotFoundException("Organization with id $organizationId not found")
        }

        var organization = objectMapper.readValue(response.body(), Organization::class.java)

        organization.employees.forEach {
            fireEmployee(requireNotNull(it.id))
        }
    }

    override fun hireEmployee(organizationId: Long, employee: Employee) {
        val employeeRequest = EmployeeRequest(employee.name, organizationId)
        val request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(employeeRequest)))
            .header("Content-Type", "application/json")
            .uri(URI.create(HIRE_EMPLOYEE_URL))
            .build()

        val response = organizationServiceHttpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response?.statusCode() != 200) {
            throw RuntimeException("Failed to hire employee $employee")
        }
    }

    private fun fireEmployee(employeeId: Long) {
        val request = HttpRequest.newBuilder()
            .DELETE()
            .uri(URI.create(FIRE_EMPLOYEE_URL.format(employeeId)))
            .build()

        val response = organizationServiceHttpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response?.statusCode() != 200) {
            throw RuntimeException("Failed to fire employee with id $employeeId")
        }
    }

    companion object {
        private const val ORGANIZATION_SERVICE_URL = "https://organization-service:8181/organization-service"
        private const val GET_ORGANIZATION_BY_ID_URL = "$ORGANIZATION_SERVICE_URL/organizations/%d"
        private const val FIRE_EMPLOYEE_URL = "$ORGANIZATION_SERVICE_URL/employee/%d"
        private const val HIRE_EMPLOYEE_URL = "$ORGANIZATION_SERVICE_URL/employee"
    }
}