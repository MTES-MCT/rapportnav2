package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AuthorityType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiJpeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiPartyEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiVesselEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiDocumentEntity
import java.time.Instant

/** A SATI document of a sea control, to be copied with the data under test. */
val satiDocument = SatiDocumentEntity(
    module = SatiModuleType.M1,
    vesselName = "FLEUR DE LYS",
    serviceName = "ULAM 56",
    inspectorFirstName = "Jean",
    inspectorLastName = "Dupont",
    inspectorAuthorityType = AuthorityType.MEMBER_FR,
    startDatetimeUtc = Instant.parse("2026-12-31T08:15:00Z"),
    endDatetimeUtc = Instant.parse("2026-12-31T10:45:00Z"),
    latitude = 47.80975,
    longitude = -3.909717,
    vessel = SatiVesselEntity(
        name = "FLEUR DE LYS",
        immat = "FRA000123456",
        extRef = "FL854548",
        ircs = "FQHZ",
        imo = "9123456",
        length = 24.5,
        type = "Chalutier",
        jpe = SatiJpeEntity(tripNumber = "TRIP-42"),
        owner = SatiPartyEntity(
            contact = ContactEntity(
                fullName = "Armement Dupont",
                nationality = "FRA",
                phone = "0601020304",
                email = "armement@example.fr",
                address = AddressEntity(fullAddress = "1 quai des Indes, 56100 Lorient"),
            )
        ),
        master = SatiPartyEntity(
            contact = ContactEntity(fullName = "Yann Le Goff", firstName = "Yann", lastName = "Le Goff")
        ),
    ),
)
