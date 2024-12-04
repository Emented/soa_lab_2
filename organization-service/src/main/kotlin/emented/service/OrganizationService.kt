package emented.service

import emented.model.domain.Organization
import emented.model.domain.OrganizationType
import emented.model.domain.search.OrganizationsSearchRequest
import emented.model.domain.search.OrganizationSearchResponse

interface OrganizationService {
    fun createOrganization(organization: Organization): Organization?
    fun getOrganizationById(organizationId: Long): Organization?
    fun deleteOrganizationById(organizationId: Long)
    fun updateOrganizationById(organizationId: Long, organization: Organization)
    fun searchOrganizations(organizationsSearchRequest: OrganizationsSearchRequest): OrganizationSearchResponse
    fun deleteOrganizationsByType(type: OrganizationType): Long
    fun getMinOrganizationByType(): Organization?
    fun getOrganizationCountByOfficialAddress(officialAddress: String): Long
}
