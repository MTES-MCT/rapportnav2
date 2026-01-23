package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Repository
class APIEnvMissionRepositoryV2(
    private val mapper: JsonMapper,
    clientFactory: HttpClientFactory,
    @param:Value("\${MONITORENV_HOST}") private val host: String,
    ): IEnvMissionRepository {

    private val logger: Logger = LoggerFactory.getLogger(APIEnvMissionRepositoryV2::class.java)


    private val client = clientFactory.create()

    override fun createMission(mission: MissionEnv): MissionEnvEntity? {
        val url = "$host/api/v1/missions"
        logger.info("Sending POST request for Env mission creation URL: $url")
        try {
            val json = mapper.writeValueAsString(mission) ?: ""

            logger.info("Body request for Mission env create as json : $json}")
            logger.info("Body request for Mission env create as entity : $mission}")

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("POST", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            logger.info("Response received, Status code: ${response.statusCode()}")

            val body = response.body()
            logger.info("Response received, Content: $body")

            if (response.statusCode() !in 200..299) {
                throw BackendInternalException(
                    message = "MonitorEnv API returned status=${response.statusCode()} for POST mission creation"
                )
            }

            val missionDataOutput: MissionDataOutput? = mapper.readValue(body)
            return missionDataOutput?.toMissionEnvEntity()
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to create Env mission",
                originalException = e
            )
        }
    }

    override fun update(mission: MissionEnvEntity): MissionEnvEntity? {
        val url = "$host/api/v1/missions/${mission.id}"
        logger.info("Sending POST request for Env mission update URL: $url")
        try {
            val json = mapper.writeValueAsString(mission) ?: ""
            logger.info("Body request for Mission env update as json : $json")
            logger.info("Body request for Mission env update as entity : $mission")

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("POST", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            logger.info("Response received, Status code: ${response.statusCode()}")

            val body = response.body()
            logger.info("Response received, Content: $body")

            if (response.statusCode() !in 200..299) {
                throw BackendInternalException(
                    message = "MonitorEnv API returned status=${response.statusCode()} for POST mission update id=${mission.id}"
                )
            }

            val missionDataOutput: MissionDataOutput? = mapper.readValue(body)
            return missionDataOutput?.toMissionEnvEntity()
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to update Env mission id=${mission.id}",
                originalException = e
            )
        }
    }

    override fun patchMission(missionId: Int, mission: PatchMissionInput): MissionEnvEntity? {
        val url = "$host/api/v2/missions/$missionId"
        logger.info("Sending PATCH request for Env mission id=$missionId. URL: $url")
        try {
            val json = mapper.writeValueAsString(mission) ?: ""
            logger.info("Body request for Mission env patch as json : $json")
            logger.info("Body request for Mission env patch as entity : $mission")

            val request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            logger.debug("Response received, missionId: ${missionId}, Status code: ${response.statusCode()}")

            val body = response.body()
            logger.info("Response received, Content: $body")

            if (response.statusCode() !in 200..299) {
                throw BackendInternalException(
                    message = "MonitorEnv API returned status=${response.statusCode()} for PATCH mission id=$missionId"
                )
            }

            val missionDataOutput: MissionDataOutput? = mapper.readValue(body)
            return missionDataOutput?.toMissionEnvEntity()
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to PATCH Env mission id=$missionId",
                originalException = e
            )
        }
    }

    override fun deleteMission(missionId: Int) {
        val url = "$host/api/v2/missions/$missionId"
        logger.info("Sending DELETE request for Env mission id=$missionId. URL: $url")
        try {
            val request = HttpRequest
                .newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(url))
                .DELETE()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            logger.debug("Response received, missionId: ${missionId}, Status code: ${response.statusCode()}")

            val body = response.body()
            logger.info("Response received, Content: $body")

            if (response.statusCode() !in 200..299) {
                throw BackendInternalException(
                    message = "MonitorEnv API returned status=${response.statusCode()} for DELETE mission id=$missionId"
                )
            }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to DELETE Env mission id=$missionId",
                originalException = e
            )
        }
    }

}
