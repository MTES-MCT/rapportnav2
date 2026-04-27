package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.CreateOrUpdateFishAuction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.DeleteFishAuction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.GetFishAuctions
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.models.AdminFishAuction
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/fish_auctions")
class FishAuctionAdminController(
    private val getFishAuctions: GetFishAuctions,
    private val createOrUpdateFishAuction: CreateOrUpdateFishAuction,
    private val deleteFishAuction: DeleteFishAuction
) {
    private val logger = LoggerFactory.getLogger(FishAuctionAdminController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of fish auctions")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of fish auctions", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = AdminFishAuction::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any fish auctions", content = [Content()])
        ]
    )
    fun getFishAuctions(): List<AdminFishAuction> {
        return getFishAuctions.execute(includeDeleted = true).map { AdminFishAuction.fromFishAuctionEntity(it) }
    }

    @PostMapping("")
    @Operation(summary = "Create FishAuction")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create FishAuction", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AdminFishAuction::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create FishAuction", content = [Content()])
        ]
    )
    fun create(@RequestBody body: AdminFishAuction): AdminFishAuction? {
        return try {
            AdminFishAuction.fromFishAuctionEntity(createOrUpdateFishAuction.execute(body.toFishAuctionEntity()))
        } catch (e: Exception) {
            logger.error("Error while creating FishAuction : ", e)
            return null
        }
    }

    @PutMapping("")
    @Operation(summary = "Update FishAuction")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Update FishAuction", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AdminFishAuction::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not update FishAuction", content = [Content()])
        ]
    )
    fun update(@RequestBody body: AdminFishAuction): AdminFishAuction? {
        return try {
            AdminFishAuction.fromFishAuctionEntity(createOrUpdateFishAuction.execute(body.toFishAuctionEntity()))
        } catch (e: Exception) {
            logger.error("Error while updating FishAuction : ", e)
            return null
        }
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a FishAuction")
    @ApiResponse(responseCode = "404", description = "Could not delete FishAuction", content = [Content()])
    fun delete(@PathVariable(name = "id") id: Int) {
        return try {
            deleteFishAuction.execute(id = id)
        } catch (e: Exception) {
            logger.error("Error while deleting FishAuction : ", e)
        }
    }
}
