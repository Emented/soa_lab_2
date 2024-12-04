package emented.model.client

import java.time.OffsetDateTime

data class Organization(
    val id: Long?,
    val name: String,
    val officialAddress: Address,
    val coordinates: Coordinates,
    val annualTurnover: Int?,
    val type: OrganizationType?,
    val employees: List<Employee>,
    val creationDate: OffsetDateTime?,
)