package emented.model.domain.search

data class FilterStrategy(
    val filterColumn: OrganizationField,
    val filterType: FilterType,
    val filterValue: String,
)