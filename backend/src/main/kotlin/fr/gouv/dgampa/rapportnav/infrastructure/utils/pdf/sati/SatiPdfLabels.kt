package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.DiscardReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.WireType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AuthorityType

/** French labels of the SATI pdf: those of the app, or of MonitorFish for its own data. */
internal object SatiPdfLabels {
    const val ADMINISTRATION = "Affaires Maritimes"
    const val MEMBER_STATE = "FRA-France"

    const val NOTE = "Ce document est un compte rendu consolidé (avec de potentielles vérifications " +
        "a posteriori) du contrôle réalisé par l'unité d'inspection. Ce compte rendu est transmis à la personne " +
        "contrôlée pour sa bonne information. Si le contrôle n'est pas conforme, ce document ne constitue en " +
        "aucun cas une pièce de procédure / un procès-verbal d'infraction."
}

internal val InfractionType.label: String
    get() = when (this) {
        InfractionType.WITH_RECORD -> "Verbalisation (PV)"
        InfractionType.WITHOUT_RECORD -> "Rappel à la réglementation"
        InfractionType.PENDING -> "En attente"
    }

// short enough to fit in a tag
internal val ControlType.label: String
    get() = when (this) {
        ControlType.ADMINISTRATIVE -> "Administratif navire"
        ControlType.GENS_DE_MER -> "Administratif gens de mer"
        ControlType.NAVIGATION -> "Règles de navigation"
        ControlType.SECURITY -> "Équipements et normes de sécurité"
        ControlType.SECTOR -> "Filière"
        ControlType.TRANSPORT -> "Transport"
        ControlType.LANDING_OBLIGATION -> "Obligation de débarquement"
        ControlType.FISHING_REPORTING_OBLIGATION -> "Obligations déclaratives pêche"
        ControlType.TECHNICAL_MEASURE -> "Mesures techniques et de conservation"
        ControlType.INN_ACTIVITY -> "Activités INN"
        ControlType.OTHER -> "Autre"
    }

// labels of MonitorFish
internal val DiscardReason.label: String
    get() = when (this) {
        DiscardReason.DIM -> "de minimis"
        DiscardReason.RET -> "espèces interdites"
        DiscardReason.DIS -> "autres rejets"
    }

// labels of the inspector form
internal val AuthorityType.label: String
    get() = when (this) {
        AuthorityType.MEMBER_FR -> "Etat membre (FR)"
        AuthorityType.AECP -> "AECP"
        AuthorityType.OTHERS -> "Autre"
    }

internal val WireType.label: String
    get() = when (this) {
        WireType.SINGLE -> "Simple"
        WireType.MANY -> "Multiple"
    }
