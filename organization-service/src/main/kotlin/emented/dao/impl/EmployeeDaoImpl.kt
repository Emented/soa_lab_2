package emented.dao.impl

import emented.dao.EmployeeDao
import emented.jooq.main.tables.references.EMPLOYEE
import emented.model.domain.Employee
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class EmployeeDaoImpl(
    private val dslContext: DSLContext
) : EmployeeDao {

    override fun create(employee: Employee, organizationId: Long?): Employee? {
        return dslContext.insertInto(EMPLOYEE)
            .set(EMPLOYEE.NAME, employee.name)
            .set(EMPLOYEE.ORGANIZATION_ID, organizationId)
            .returning(employeeFields)
            .fetchOne {
                mapEmployee(it)
            }
    }

    override fun getById(employeeId: Long): Employee? {
        return dslContext.select(employeeFields)
            .from(EMPLOYEE)
            .where(EMPLOYEE.ID.eq(employeeId))
            .fetchOne {
                mapEmployee(it)
            }
    }

    override fun deleteById(employeeId: Long) {
        dslContext.delete(EMPLOYEE)
            .where(EMPLOYEE.ID.eq(employeeId))
            .execute()
    }

    override fun deleteByOrganizationId(organizationId: Long) {
        dslContext.delete(EMPLOYEE)
            .where(EMPLOYEE.ORGANIZATION_ID.eq(organizationId))
            .execute()
    }

    companion object {
        val employeeFields = setOf(
            EMPLOYEE.ID,
            EMPLOYEE.NAME,
        )

        fun mapEmployee(record: Record): Employee {
            return Employee(
                id = record.get(EMPLOYEE.ID),
                name = record.get(EMPLOYEE.NAME)!!,
            )
        }
    }
}