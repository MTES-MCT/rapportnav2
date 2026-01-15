package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output

data class NatinfDataOutput(
    val label: String,
    val value: Int,
)

data class ThreatCharacterizationDataOutput(
    val children: List<NatinfDataOutput>,
    val label: String,
    val value: String,
)

data class ThreatHierarchyDataOutput(
    val children: List<ThreatCharacterizationDataOutput>,
    val label: String,
    val value: String,
)
