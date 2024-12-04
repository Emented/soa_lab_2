package emented.extensions

import emented.model.Address
import emented.model.Coordinates
import emented.model.Employee
import emented.model.Organization
import emented.model.OrganizationType
import emented.model.OrganizationsWithPagerResponse
import emented.model.Pager
import emented.model.domain.Address as DomainAddress
import emented.model.domain.Coordinates as DomainCoordinates
import emented.model.domain.Organization as DomainOrganization
import emented.model.domain.OrganizationType as DomainOrganizationType
import emented.model.domain.search.OrganizationSearchResponse as DomainOrganizationSearchResponse
import emented.model.domain.Employee as DomainEmployee

fun DomainOrganization.toResponse(): Organization {
    return Organization(
        id = id!!,
        name = name,
        coordinates = coordinates.toResponse(),
        officialAddress = officialAddress.toResponse(),
        type = type?.toResponse(),
        creationDate = creationDate!!,
        annualTurnover = annualTurnover,
        employees = employees.map { it.toResponse() },
    )
}

fun DomainCoordinates.toResponse(): Coordinates {
    return Coordinates(
        x = x,
        y = y,
    )
}

fun DomainAddress.toResponse(): Address {
    return Address(
        zipCode = zipCode,
    )
}

fun DomainOrganizationType.toResponse(): OrganizationType {
    return when (this) {
        DomainOrganizationType.PUBLIC -> OrganizationType.PUBLIC
        DomainOrganizationType.TRUST -> OrganizationType.TRUST
        DomainOrganizationType.GOVERNMENT -> OrganizationType.GOVERNMENT
        DomainOrganizationType.OPEN_JOINT_STOCK_COMPANY -> OrganizationType.OPEN_JOINT_STOCK_COMPANY
        DomainOrganizationType.PRIVATE_LIMITED_COMPANY -> OrganizationType.PRIVATE_LIMITED_COMPANY
    }
}

fun DomainOrganizationSearchResponse.toResponse(): OrganizationsWithPagerResponse {
    return OrganizationsWithPagerResponse(
        pager = Pager(
            pageNum = pageNum,
            totalPages = totalPages,
        ),
        organizations = organizations.map { it.toResponse() }
    )
}

fun DomainEmployee.toResponse(): Employee {
    return Employee(
        id = id!!,
        name = name,
    )
}

