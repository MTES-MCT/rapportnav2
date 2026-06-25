package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

enum class LogbookMessagePurpose {
    // TODO Find out what this purpose code means.
    ACS,

    /** Emergency. */
    ECY,

    /** Vessels grounded and called by the authorities. */
    GRD,

    /** Landing. */
    LAN,

    /** Other. */
    OTH,

    /** Refueling. */
    REF,

    /** Repair. */
    REP,

    /** Rest. */
    RES,

    /** Return for Scientific Research. */
    SCR,

    /** Sheltering. */
    SHE,

    /** Transhipment. */
    TRA,
}
