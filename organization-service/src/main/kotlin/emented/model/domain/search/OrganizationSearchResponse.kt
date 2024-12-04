package emented.model.domain.search

import emented.model.domain.Organization

data class OrganizationSearchResponse(
    val organizations: List<Organization>,
    val totalPages: Int,
    val pageNum: Int,
)
