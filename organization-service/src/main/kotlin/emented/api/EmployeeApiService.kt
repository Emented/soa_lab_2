package emented.api

import emented.extensions.toModel
import emented.extensions.toResponse
import emented.model.Employee
import emented.model.EmployeeRequest
import emented.service.EmployeeService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class EmployeeApiService(
    private val employeeService: EmployeeService
) : EmployeeApiDelegate {

    override fun createEmployee(employeeRequest: EmployeeRequest): ResponseEntity<Employee> {
        val creationResult = employeeService.createEmployee(employeeRequest.toModel(), employeeRequest.organizationId)
            ?: return ResponseEntity.badRequest().build()

        return ResponseEntity.ok(creationResult.toResponse())
    }

    override fun deleteEmployeeById(employeeId: Long): ResponseEntity<Unit> {
        employeeService.deleteEmployeeById(employeeId)

        return ResponseEntity.ok().build()
    }

    override fun getEmployeeById(employeeId: Long): ResponseEntity<Employee> {
        val employee = employeeService.getEmployeeById(employeeId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(employee.toResponse())
    }
}