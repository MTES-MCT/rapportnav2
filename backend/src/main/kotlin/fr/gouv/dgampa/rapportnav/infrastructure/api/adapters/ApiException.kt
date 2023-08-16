package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters

import org.springframework.web.server.ResponseStatusException

class ApiException(code: Int, message: String) : ResponseStatusException(code, message, null)
