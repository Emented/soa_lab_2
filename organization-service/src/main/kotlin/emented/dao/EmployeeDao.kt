package emented.dao

import emented.model.domain.Employee

interface EmployeeDao {
    fun create(employee: Employee, organizationId: Long?): Employee?
    fun getById(employeeId: Long): Employee?
    fun deleteById(employeeId: Long)
    fun deleteByOrganizationId(organizationId: Long)
}