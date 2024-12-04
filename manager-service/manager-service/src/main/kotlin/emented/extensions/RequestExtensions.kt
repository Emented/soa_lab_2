package emented.extensions

import emented.model.Employee
import emented.model.domain.Employee as DomainEmployee

fun Employee.toModel(): DomainEmployee {
    return DomainEmployee(
        name = name,
    )
}