package emented.dao

import emented.model.domain.Organization
import emented.model.domain.OrganizationType
import emented.model.domain.search.FilterStrategy
import emented.model.domain.search.SortingStrategy

interface OrganizationDao {
    fun create(organization: Organization): Organization?
    fun getById(id: Long): Organization?
    fun deleteById(id: Long): Boolean
    fun update(organization: Organization): Boolean
    fun search(
        sortingStrategies: List<SortingStrategy>,
        filterStrategies: List<FilterStrategy>,
        size: Int,
        page: Int
    ): Pair<Int, List<Organization>>
    fun deleteAllByType(type: OrganizationType): Long
    fun getMinByType(): Organization?
    fun getCountByOfficialAddress(officialAddress: String): Long
}