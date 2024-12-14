package emented.dao.impl

import emented.dao.OrganizationDao
import emented.jooq.main.tables.records.OrganizationRecord
import emented.jooq.main.tables.references.EMPLOYEE
import emented.jooq.main.tables.references.ORGANIZATION
import emented.model.domain.Address
import emented.model.domain.Coordinates
import emented.model.domain.Employee
import emented.model.domain.Organization
import emented.model.domain.OrganizationType
import emented.model.domain.search.FilterStrategy
import emented.model.domain.search.FilterType
import emented.model.domain.search.OrganizationField
import emented.model.domain.search.SortingStrategy
import emented.model.domain.search.SortingType
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SortField
import org.jooq.TableField
import org.jooq.impl.DSL.multiset
import org.jooq.impl.DSL.select
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.ArrayList
import kotlin.math.ceil

@Repository
class OrganizationDaoImpl(
    private val dslContext: DSLContext
) : OrganizationDao {

    override fun create(organization: Organization): Organization? {
        return dslContext.insertInto(ORGANIZATION)
            .set(ORGANIZATION.NAME, organization.name)
            .set(ORGANIZATION.OFFICIAL_ADDRESS_ZIP, organization.officialAddress.zipCode)
            .set(ORGANIZATION.X_COORDINATE, organization.coordinates.x)
            .set(ORGANIZATION.Y_COORDINATE, organization.coordinates.y.toDouble())
            .set(ORGANIZATION.ANNUAL_TURNOVER, organization.annualTurnover)
            .set(ORGANIZATION.TYPE, organization.type?.name)
            .set(ORGANIZATION.CREATED_AT, OffsetDateTime.now())
            .returning(emptyOrganizationFields)
            .fetchOne {
                mapEmptyOrganization(it, organization.employees)
            }
    }

    override fun getById(id: Long): Organization? {
        return dslContext.select(organizationFields)
            .from(ORGANIZATION)
            .where(ORGANIZATION.ID.eq(id))
            .fetchOne {
                mapOrganization(it)
            }
    }

    override fun deleteById(id: Long): Boolean {
        dslContext.deleteFrom(EMPLOYEE)
            .where(EMPLOYEE.ORGANIZATION_ID.eq(id))
            .execute()

        return dslContext.deleteFrom(ORGANIZATION)
            .where(ORGANIZATION.ID.eq(id))
            .execute() > 0
    }

    override fun update(organization: Organization): Boolean {
        return dslContext.update(ORGANIZATION)
            .set(ORGANIZATION.NAME, organization.name)
            .set(ORGANIZATION.OFFICIAL_ADDRESS_ZIP, organization.officialAddress.zipCode)
            .set(ORGANIZATION.X_COORDINATE, organization.coordinates.x)
            .set(ORGANIZATION.Y_COORDINATE, organization.coordinates.y.toDouble())
            .set(ORGANIZATION.ANNUAL_TURNOVER, organization.annualTurnover)
            .set(ORGANIZATION.TYPE, organization.type?.name)
            .where(ORGANIZATION.ID.eq(organization.id))
            .execute() > 0
    }

    override fun search(
        sortingStrategies: List<SortingStrategy>,
        filterStrategies: List<FilterStrategy>,
        size: Int,
        page: Int
    ): Pair<Int, List<Organization>> {
        val totalRecords = dslContext.fetchCount(dslContext.selectFrom(ORGANIZATION))
        val totalPages = ceil(totalRecords.toDouble() / size).toInt()
        val offset = (page - 1) * size

        val foundOrganizations = dslContext.select(organizationFields)
            .from(ORGANIZATION)
            .where(filterStrategies.map { it.toCondition() })
            .orderBy(sortingStrategies.map { it.toOrderBy() })
            .limit(size)
            .offset(offset)
            .fetch {
                mapOrganization(it)
            }

        return totalPages to foundOrganizations
    }

    override fun deleteAllByType(type: OrganizationType): Long {
        val organizationIds = dslContext.select(ORGANIZATION.ID)
            .from(ORGANIZATION)
            .where(ORGANIZATION.TYPE.eq(type.name))
            .fetch { it.value1() }
            .toSet()

        dslContext.deleteFrom(EMPLOYEE)
            .where(EMPLOYEE.ORGANIZATION_ID.`in`(organizationIds))
            .execute()

        return dslContext.deleteFrom(ORGANIZATION)
            .where(ORGANIZATION.ID.`in`(organizationIds))
            .execute().toLong()
    }

    override fun getMinByType(): Organization? {
        return dslContext.select(organizationFields)
            .from(ORGANIZATION)
            .orderBy(ORGANIZATION.ANNUAL_TURNOVER.asc())
            .limit(1)
            .fetchOne {
                mapOrganization(it)
            }
    }

    override fun getCountByOfficialAddress(officialAddress: String): Long {
        return dslContext.selectCount()
            .from(ORGANIZATION)
            .where(ORGANIZATION.OFFICIAL_ADDRESS_ZIP.eq(officialAddress))
            .fetchOne(0, Long::class.java)!!
    }

    fun FilterStrategy.toCondition(): Condition {
        val field = this.filterColumn.toJooqColumn()
        val castedField = field.cast(String::class.java)

        return when (filterType) {
            FilterType.EQUALS -> castedField.eq(filterValue)
            FilterType.CONTAINS -> castedField.like("%$filterValue%")
            FilterType.LESS -> castedField.lt(filterValue)
            FilterType.MORE -> castedField.gt(filterValue)
            FilterType.LESS_OR_EQUALS -> castedField.le(filterValue)
            FilterType.MORE_OR_EQUALS -> castedField.ge(filterValue)
        }
    }

    fun SortingStrategy.toOrderBy(): SortField<*> {
        val field = this.sortingColumn.toJooqColumn()

        return when (this.sortingType) {
            SortingType.ASC -> field.asc()
            SortingType.DESC -> field.desc()
        }
    }

    fun OrganizationField.toJooqColumn(): TableField<OrganizationRecord, *> {
        return when (this) {
            OrganizationField.ID -> ORGANIZATION.ID
            OrganizationField.NAME -> ORGANIZATION.NAME
            OrganizationField.OFFICIAL_ADDRESS -> ORGANIZATION.OFFICIAL_ADDRESS_ZIP
            OrganizationField.ANNUAL_TURNOVER -> ORGANIZATION.ANNUAL_TURNOVER
            OrganizationField.TYPE -> ORGANIZATION.TYPE
            OrganizationField.CREATION_DATE -> ORGANIZATION.CREATED_AT
        }
    }

    companion object {
        val organizationFields = setOf(
            ORGANIZATION.ID,
            ORGANIZATION.NAME,
            ORGANIZATION.OFFICIAL_ADDRESS_ZIP,
            ORGANIZATION.X_COORDINATE,
            ORGANIZATION.Y_COORDINATE,
            ORGANIZATION.ANNUAL_TURNOVER,
            ORGANIZATION.TYPE,
            ORGANIZATION.CREATED_AT,
            multiset(
                select(EmployeeDaoImpl.employeeFields)
                    .from(EMPLOYEE)
                    .where(EMPLOYEE.ORGANIZATION_ID.eq(ORGANIZATION.ID))
            ).`as`("organizationEmployees").convertFrom { it.map { EmployeeDaoImpl.mapEmployee(it) } }
        )

        val emptyOrganizationFields = setOf(
            ORGANIZATION.ID,
            ORGANIZATION.NAME,
            ORGANIZATION.OFFICIAL_ADDRESS_ZIP,
            ORGANIZATION.X_COORDINATE,
            ORGANIZATION.Y_COORDINATE,
            ORGANIZATION.ANNUAL_TURNOVER,
            ORGANIZATION.TYPE,
            ORGANIZATION.CREATED_AT,
        )

        fun mapOrganization(record: Record): Organization {
            return Organization(
                id = record.get(ORGANIZATION.ID),
                name = record.get(ORGANIZATION.NAME)!!,
                officialAddress = Address(record.get(ORGANIZATION.OFFICIAL_ADDRESS_ZIP)!!),
                coordinates = Coordinates(
                    record.get(ORGANIZATION.X_COORDINATE)!!,
                    record.get(ORGANIZATION.Y_COORDINATE)!!.toFloat(),
                ),
                annualTurnover = record.get(ORGANIZATION.ANNUAL_TURNOVER),
                type = record.get(ORGANIZATION.TYPE)?.let {
                    OrganizationType.valueOf(it)
                },
                employees = record.get("organizationEmployees") as ArrayList<Employee>,
                creationDate = record.get(ORGANIZATION.CREATED_AT),
            )
        }

        fun mapEmptyOrganization(record: Record, employees: List<Employee>): Organization {
            return Organization(
                id = record.get(ORGANIZATION.ID),
                name = record.get(ORGANIZATION.NAME)!!,
                officialAddress = Address(record.get(ORGANIZATION.OFFICIAL_ADDRESS_ZIP)!!),
                coordinates = Coordinates(
                    record.get(ORGANIZATION.X_COORDINATE)!!,
                    record.get(ORGANIZATION.Y_COORDINATE)!!.toFloat(),
                ),
                annualTurnover = record.get(ORGANIZATION.ANNUAL_TURNOVER),
                type = record.get(ORGANIZATION.TYPE)?.let {
                    OrganizationType.valueOf(it)
                },
                employees = employees,
                creationDate = record.get(ORGANIZATION.CREATED_AT),
            )
        }
    }
}