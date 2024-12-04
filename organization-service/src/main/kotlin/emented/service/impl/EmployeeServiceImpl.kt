package emented.service.impl

import emented.dao.EmployeeDao
import emented.model.domain.Employee
import emented.service.EmployeeService
import org.springframework.stereotype.Service

@Service
class EmployeeServiceImpl(
    private val employeeDao: EmployeeDao
) : EmployeeService {
    override fun createEmployee(employee: Employee, organizationId: Long?): Employee? {
        return employeeDao.create(employee, organizationId)
    }

    override fun deleteEmployeeById(employeeId: Long) {
        return employeeDao.deleteById(employeeId)
    }

    override fun getEmployeeById(employeeId: Long): Employee? {
        return employeeDao.getById(employeeId)
    }
}