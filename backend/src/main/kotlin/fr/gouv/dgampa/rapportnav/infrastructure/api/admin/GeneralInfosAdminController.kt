package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllGeneralInfos
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateGeneralInfoAdmin
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.input.AdminGeneralInfosUpdateInput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/general-infos")
class GeneralInfosAdminController(
    private val updateGeneralInfo: UpdateGeneralInfoAdmin,
    private val getAllGeneralInfos: GetAllGeneralInfos,
) {
    @GetMapping("")
    @Operation(summary = "Get all general information")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "get general information's", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GeneralInfoEntity::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not get general information's", content = [Content()])
        ]
    )
    fun getAll(): List<GeneralInfoEntity> {
        return getAllGeneralInfos.execute().map { GeneralInfoEntity.fromGeneralInfoModel(it) }
    }

    @PutMapping()
    @Operation(summary = "Update general information, by mission id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "update general information's", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GeneralInfoEntity::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not update general information's", content = [Content()])
        ]
    )
    fun update(
        @RequestBody generalInfo: AdminGeneralInfosUpdateInput
    ): GeneralInfoEntity? {
        return updateGeneralInfo.execute(generalInfo = generalInfo)
    }
}
