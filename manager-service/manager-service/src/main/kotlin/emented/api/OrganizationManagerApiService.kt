package emented.api

import emented.extensions.toModel
import emented.model.Employee
import emented.service.OrganizationManagerService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class OrganizationManagerApiService(
    private val organizationManagerService: OrganizationManagerService
) : OrganizationManagerApiDelegate {

    override fun fireAllOrganizationEmployees(organizationId: Long): ResponseEntity<Unit> {
        organizationManagerService.fireAllEmployees(organizationId)
        return ResponseEntity.ok().build()
    }

    override fun hireEmployeeToOrganization(
        organizationId: Long,
        employee: Employee
    ): ResponseEntity<Unit> {
        organizationManagerService.hireEmployee(organizationId, employee.toModel())
        return ResponseEntity.ok().build()
    }
}