package emented.api

import emented.extensions.toModel
import emented.extensions.toResponse
import emented.model.Organization
import emented.model.OrganizationRequest
import emented.model.OrganizationsSearchRequest
import emented.model.OrganizationsWithPagerResponse
import emented.service.OrganizationService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class OrganizationApiService(
    private val organizationService: OrganizationService,
) : OrganizationApiDelegate {

    override fun createOrganization(
        organizationRequest: OrganizationRequest
    ): ResponseEntity<Organization> {
        val creationResult = organizationService.createOrganization(organizationRequest.toModel())
            ?: return ResponseEntity.badRequest().build()

        return ResponseEntity.ok(creationResult.toResponse())
    }

    override fun getOrganizationById(
        organizationId: Long
    ): ResponseEntity<Organization> {
        val organization = organizationService.getOrganizationById(organizationId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(organization.toResponse())
    }

    override fun deleteOrganizationById(
        organizationId: Long
    ): ResponseEntity<Unit> {
        organizationService.deleteOrganizationById(organizationId)

        return ResponseEntity.ok().build()
    }

    override fun updateOrganizationById(
        organizationId: Long,
        organizationRequest: OrganizationRequest
    ): ResponseEntity<Unit> {
        organizationService.updateOrganizationById(organizationId, organizationRequest.toModel())

        return ResponseEntity.ok().build()
    }

    override fun searchOrganizations(
        organizationsSearchRequest: OrganizationsSearchRequest
    ): ResponseEntity<OrganizationsWithPagerResponse> {
        return ResponseEntity.ok(
            organizationService.searchOrganizations(organizationsSearchRequest.toModel()).toResponse()
        )
    }
}