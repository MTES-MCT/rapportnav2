package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.Mission
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetEnvMissions
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class MissionController(
    private val getEnvMissions: GetEnvMissions,
) {

     @QueryMapping
    fun missions(@Argument userId: Any): List<Mission> {
        val missions = getEnvMissions.execute(
            userId = null,
            startedAfterDateTime = null,
            startedBeforeDateTime = null,
            pageNumber = null,
            pageSize = null,
        )
        return missions
//        return missions.map { MissionDataOutput.fromMission(it) }
//        return postDao.getRecentPosts(count, offset)
    }
}
