package emented.extensions

import emented.model.Address
import emented.model.Coordinates
import emented.model.EmployeeRequest
import emented.model.FilterStrategy
import emented.model.FilterType
import emented.model.OrganizationField
import emented.model.OrganizationRequest
import emented.model.OrganizationType
import emented.model.OrganizationsSearchRequest
import emented.model.SortingStrategy
import emented.model.SortingType
import emented.model.domain.Address as DomainAddress
import emented.model.domain.Coordinates as DomainCoordinates
import emented.model.domain.Organization as DomainOrganization
import emented.model.domain.OrganizationType as DomainOrganizationType
import emented.model.domain.Employee as DomainEmployee
import emented.model.domain.search.OrganizationsSearchRequest as DomainOrganizationsSearchRequest
import emented.model.domain.search.SortingStrategy as DomainSortingStrategy
import emented.model.domain.search.FilterStrategy as DomainFilterStrategy
import emented.model.domain.search.FilterType as DomainFilterType
import emented.model.domain.search.SortingType as DomainSortingType
import emented.model.domain.search.OrganizationField as DomainOrganizationField

fun OrganizationRequest.toModel(): DomainOrganization {
    return DomainOrganization(
        id = null,
        name = name,
        officialAddress = officialAddress.toModel(),
        coordinates = coordinates.toModel(),
        annualTurnover = annualTurnover,
        type = type?.toModel(),
        employees = listOf(),
        creationDate = null,
    )
}

fun Address.toModel(): DomainAddress {
    return DomainAddress(
        zipCode = zipCode
    )
}

fun Coordinates.toModel(): DomainCoordinates {
    return DomainCoordinates(
        x = x,
        y = y,
    )
}

fun OrganizationType.toModel(): DomainOrganizationType {
    return when (this) {
        OrganizationType.PUBLIC -> DomainOrganizationType.PUBLIC
        OrganizationType.TRUST -> DomainOrganizationType.TRUST
        OrganizationType.GOVERNMENT -> DomainOrganizationType.GOVERNMENT
        OrganizationType.OPEN_JOINT_STOCK_COMPANY -> DomainOrganizationType.OPEN_JOINT_STOCK_COMPANY
        OrganizationType.PRIVATE_LIMITED_COMPANY -> DomainOrganizationType.PRIVATE_LIMITED_COMPANY
    }
}

fun OrganizationsSearchRequest.toModel(): DomainOrganizationsSearchRequest {
    return DomainOrganizationsSearchRequest(
        sortingStrategies = sortingStrategies.map { it.toModel() }.distinctBy { it.sortingColumn },
        filterStrategies = filterStrategies.map { it.toModel() }.distinctBy { it.filterColumn },
        page = page!!,
        size = propertySize!!,
    )
}

fun SortingStrategy.toModel(): DomainSortingStrategy {
    return DomainSortingStrategy(
        sortingType = sortingType.toModel(),
        sortingColumn = sortingColumn.toModel(),
    )
}

fun FilterStrategy.toModel(): DomainFilterStrategy {
    return DomainFilterStrategy(
        filterType = filterType.toModel(),
        filterColumn = filterColumn.toModel(),
        filterValue = filterValue,
    )
}

fun OrganizationField.toModel(): DomainOrganizationField {
    return when (this) {
        OrganizationField.ID -> DomainOrganizationField.ID
        OrganizationField.NAME -> DomainOrganizationField.NAME
        OrganizationField.ANNUAL_TURNOVER -> DomainOrganizationField.ANNUAL_TURNOVER
        OrganizationField.TYPE -> DomainOrganizationField.TYPE
        OrganizationField.CREATION_DATE -> DomainOrganizationField.CREATION_DATE
        OrganizationField.OFFICIAL_ADDRESS -> DomainOrganizationField.OFFICIAL_ADDRESS
    }
}

fun SortingType.toModel(): DomainSortingType {
    return when (this) {
        SortingType.ASC -> DomainSortingType.ASC
        SortingType.DESC -> DomainSortingType.DESC
    }
}

fun FilterType.toModel(): DomainFilterType {
    return when (this) {
        FilterType.EQUALS -> DomainFilterType.EQUALS
        FilterType.LESS -> DomainFilterType.LESS
        FilterType.MORE -> DomainFilterType.MORE
        FilterType.CONTAINS -> DomainFilterType.CONTAINS
        FilterType.LESS_OR_EQUALS -> DomainFilterType.LESS_OR_EQUALS
        FilterType.MORE_OR_EQUALS -> DomainFilterType.MORE_OR_EQUALS
    }
}

fun EmployeeRequest.toModel(): DomainEmployee {
    return DomainEmployee(
        id = null,
        name = name,
    )
}