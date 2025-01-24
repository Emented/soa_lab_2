package emented.extensions

import emented.soa.Employee
import emented.model.domain.Employee as DomainEmployee

fun Employee.toModel(): DomainEmployee {
    return DomainEmployee(
        name = name,
    )
}