package emented.api

import emented.extensions.toModel
import emented.service.OrganizationManagerService
import emented.soa.FireAllEmployeesRequest
import emented.soa.FireAllEmployeesResponse
import emented.soa.HireEmployeeRequest
import emented.soa.HireEmployeeResponse
import emented.soa.PingRequest
import emented.soa.PingResponse
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload

const val NAMESPACE_URI: String = "http://emented/soa"

@Endpoint
class OrganizationManagerApiEndpoint(
    private val organizationManagerService: OrganizationManagerService
) {

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "pingRequest")
    @ResponsePayload
    fun ping(
        @RequestPayload pingRequest: PingRequest
    ): PingResponse {
        val response = PingResponse()
        response.result = "PONG"
        return response
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "hireEmployeeRequest")
    @ResponsePayload
    fun hireEmployeeToOrganization(
        @RequestPayload hireEmployeeRequest: HireEmployeeRequest
    ): HireEmployeeResponse {
        organizationManagerService.hireEmployee(
            hireEmployeeRequest.organizationId,
            hireEmployeeRequest.employee.toModel()
        )

        val response = HireEmployeeResponse()
        response.isHired = true
        response.result = "Employee hired"
        return response
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "fireAllEmployeesRequest")
    @ResponsePayload
    fun fireAllEmployeesFromOrganization(
        @RequestPayload fireAllEmployeesRequest: FireAllEmployeesRequest
    ): FireAllEmployeesResponse {
        val result = organizationManagerService.fireAllEmployees(fireAllEmployeesRequest.organizationId)

        val response = FireAllEmployeesResponse()
        response.isFired = result
        response.result = "Employees fired"
        return response
    }
}