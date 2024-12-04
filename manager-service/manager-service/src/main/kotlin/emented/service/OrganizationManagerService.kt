package emented.service

import emented.model.domain.Employee

interface OrganizationManagerService {
    fun fireAllEmployees(organizationId: Long)
    fun hireEmployee(organizationId: Long, employee: Employee)
}