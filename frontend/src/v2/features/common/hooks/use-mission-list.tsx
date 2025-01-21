import { Mission2, MissionListItem } from '../types/mission-types'
import { useDate } from './use-date'
import { getOpenByText } from './use-mission-tag'

interface MissionListHook {
  getMissionListItem: (mission: Mission2) => MissionListItem
}

export function useMissionList(): MissionListHook {
  const { formatDateForFrenchHumans, formatDateForMissionName } = useDate()
  const formatMissionName = (startDate?: string): string => {
    return `Mission #${formatDateForMissionName(startDate)}`
  }
  const getCrewNumber = (mission: Mission2) =>
    !mission?.generalInfos?.serviceId ? '--' : mission?.generalInfos.serviceId % 2 === 0 ? 'B' : 'A'

  const getExportLabel = (
    mission: Mission2
  ) => `- ${formatMissionName(mission.envData.startDateTimeUtc)} - ${getOpenByText(mission.envData.missionSource)} -{' '}
  ${mission.actions?.length ?? 0} action(s)`

  const getMissionListItem = (mission: Mission2): MissionListItem => {
    return {
      id: mission.id,
      status: mission.status,

      openBy: mission.envData.openBy,
      crew: mission.generalInfos.crew,
      crewNumber: getCrewNumber(mission),
      exportLabel: getExportLabel(mission),
      controlUnits: mission.envData.controlUnits,
      missionSource: mission.envData.missionSource,
      completenessForStats: mission.completenessForStats,
      observationsByUnit: mission.envData.observationsByUnit,
      endDateTimeUtc: mission.envData.endDateTimeUtc,
      startDateTimeUtc: mission.envData.startDateTimeUtc,
      missionNamePam: formatMissionName(mission.envData.startDateTimeUtc),
      missionNameUlam: formatMissionName(mission.envData.startDateTimeUtc),
      endDateTimeUtcText: formatDateForFrenchHumans(mission.envData.endDateTimeUtc),
      startDateTimeUtcText: formatDateForFrenchHumans(mission.envData.startDateTimeUtc)
    }
  }

  return {
    getMissionListItem
  }
}
