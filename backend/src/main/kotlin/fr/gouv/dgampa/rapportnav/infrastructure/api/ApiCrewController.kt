package fr.gouv.dgampa.rapportnav.infrastructure.api

import fr.gouv.dgampa.rapportnav.domain.use_cases.crew.GetCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.outputs.CrewDataOutput
// import io.swagger.v3.oas.annotations.Operation
// import io.swagger.v3.oas.annotations.Parameter
// import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/crew")
// @Tag(description = "API Crew", name = "Crew")
class ApiCrewController(
    private val getCrew: GetCrew,
) {

    @GetMapping("")
    // @Operation(summary = "Get crew")
    fun getCrewController(): List<CrewDataOutput> {
        println("fdsfdsfsd")
        val crew = getCrew.execute()
        return crew.map { CrewDataOutput.fromCrew(it) }
    }

   
}
