/*
 * This file is generated by jOOQ.
 */
package emented.jooq.main


import emented.jooq.main.tables.Employee
import emented.jooq.main.tables.Organization

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Public : SchemaImpl("public", DefaultCatalog.DEFAULT_CATALOG) {
    public companion object {

        /**
         * The reference instance of <code>public</code>
         */
        val PUBLIC: Public = Public()
    }

    /**
     * The table <code>public.employee</code>.
     */
    val EMPLOYEE: Employee get() = Employee.EMPLOYEE

    /**
     * The table <code>public.organization</code>.
     */
    val ORGANIZATION: Organization get() = Organization.ORGANIZATION

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getTables(): List<Table<*>> = listOf(
        Employee.EMPLOYEE,
        Organization.ORGANIZATION
    )
}
