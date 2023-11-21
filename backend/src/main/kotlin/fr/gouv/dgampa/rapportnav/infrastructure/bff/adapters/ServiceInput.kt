package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel

data class ServiceInput(
  val id: Int?,
  val name: String,
  val serviceLinked: ServiceInput?
) {
  fun toServiceModel(): ServiceModel {
    return ServiceModel(
      id = id,
      name = name,
      serviceLinked = serviceLinked?.toServiceModel()
    )
  }
}
