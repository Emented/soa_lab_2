package emented.api

import emented.extensions.toModel
import emented.extensions.toResponse
import emented.model.CountResponse
import emented.model.Organization
import emented.model.OrganizationType
import emented.service.OrganizationService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class OrganizationUtilsApiService(
    private val organizationService: OrganizationService
) : OrganizationUtilsApiDelegate {

    override fun deleteOrganizationsByType(
        type: OrganizationType
    ): ResponseEntity<CountResponse> {
        val count = organizationService.deleteOrganizationsByType(type.toModel())
        return ResponseEntity.ok(CountResponse(count))
    }

    override fun getMinOrganizationByType(): ResponseEntity<Organization> {
        val organization = organizationService.getMinOrganizationByType()
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(organization.toResponse())
    }

    override fun getOrganizationCountByOfficialAddress(
        officialAddress: String
    ): ResponseEntity<CountResponse> {
        val count = organizationService.getOrganizationCountByOfficialAddress(officialAddress)
        return ResponseEntity.ok(CountResponse(count))
    }
}