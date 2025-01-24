package emented.service

import emented.model.domain.Employee

interface OrganizationManagerService {
    fun fireAllEmployees(organizationId: Long): Boolean
    fun hireEmployee(organizationId: Long, employee: Employee)
}