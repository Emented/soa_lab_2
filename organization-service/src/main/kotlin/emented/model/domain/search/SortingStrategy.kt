package emented.model.domain.search

data class SortingStrategy(
    val sortingType: SortingType,
    val sortingColumn: OrganizationField,
)
