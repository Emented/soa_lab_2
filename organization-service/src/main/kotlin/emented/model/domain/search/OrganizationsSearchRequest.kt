package emented.model.domain.search

data class OrganizationsSearchRequest(
    val sortingStrategies: List<SortingStrategy>,
    val filterStrategies: List<FilterStrategy>,
    val page: Int,
    val size: Int
)
