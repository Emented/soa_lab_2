package emented.service

import emented.model.domain.Employee

interface EmployeeService {
    fun createEmployee(employee: Employee, organizationId: Long?): Employee?
    fun deleteEmployeeById(employeeId: Long)
    fun getEmployeeById(employeeId: Long): Employee?
}