package emented.service.impl

import emented.dao.OrganizationDao
import emented.model.domain.Organization
import emented.model.domain.OrganizationType
import emented.model.domain.search.OrganizationSearchResponse
import emented.model.domain.search.OrganizationsSearchRequest
import emented.service.OrganizationService
import org.springframework.stereotype.Service

@Service
class OrganizationSeriviceImpl(
    private val organizationDao: OrganizationDao,
) : OrganizationService {

    override fun createOrganization(organization: Organization): Organization? {
        return organizationDao.create(organization)
    }

    override fun getOrganizationById(organizationId: Long): Organization? {
        return organizationDao.getById(organizationId)
    }

    override fun deleteOrganizationById(organizationId: Long) {
        organizationDao.deleteById(organizationId)
    }

    override fun updateOrganizationById(organizationId: Long, organization: Organization) {
        organizationDao.update(
            organization.copy(
                id = organizationId
            )
        )
    }

    override fun searchOrganizations(
        organizationsSearchRequest: OrganizationsSearchRequest
    ): OrganizationSearchResponse {
        val (totalPages, organizations) = organizationDao.search(
            organizationsSearchRequest.sortingStrategies,
            organizationsSearchRequest.filterStrategies,
            organizationsSearchRequest.size,
            organizationsSearchRequest.page,
        )

        return OrganizationSearchResponse(
            organizations = organizations,
            totalPages = totalPages,
            pageNum = organizationsSearchRequest.page,
        )
    }

    override fun deleteOrganizationsByType(type: OrganizationType): Long {
        return organizationDao.deleteAllByType(type)
    }

    override fun getMinOrganizationByType(): Organization? {
        return organizationDao.getMinByType()
    }

    override fun getOrganizationCountByOfficialAddress(officialAddress: String): Long {
        return organizationDao.getCountByOfficialAddress(officialAddress)
    }
}