import { Mission2, MissionListItem } from '../types/mission-types'
import { useDate } from './use-date'
import { useMissionTag } from './use-mission-tag'

interface MissionListHook {
  getMissionListItem: (mission: Mission2) => MissionListItem
}

export function useMissionList(): MissionListHook {
  const { getOpenByText } = useMissionTag()
  const { formatDateForFrenchHumans, formatDateForMissionName, formaDateMissionNameUlam } = useDate()
  const formatMissionName = (startDate?: string): string => {
    return `Mission #${formatDateForMissionName(startDate)}`
  }

  const formatMissionNameUlam = (startDate?: string): string => {
    return `Mission #${formaDateMissionNameUlam(startDate)}`
  }
  const getCrewNumber = (mission: Mission2) =>
    !mission?.generalInfos?.serviceId ? '--' : mission?.generalInfos.serviceId % 2 === 0 ? 'B' : 'A'

  const getExportLabel = (mission: Mission2) =>
    `- ${formatMissionName(mission.data.startDateTimeUtc)} - ${getOpenByText(mission.data.missionSource)} - ${mission.actions?.length ?? 0} action(s)`

  const getMissionListItem = (mission: Mission2): MissionListItem => {
    return {
      id: mission.id,
      status: mission.status,
      idUUID: mission.idUUID,
      openBy: mission.data.openBy,
      crew: mission.generalInfos.crew,
      crewNumber: getCrewNumber(mission),
      exportLabel: getExportLabel(mission),
      controlUnits: mission.data.controlUnits,
      missionSource: mission.data.missionSource,
      completenessForStats: mission.completenessForStats,
      observationsByUnit: mission.data.observationsByUnit,
      endDateTimeUtc: mission.data.endDateTimeUtc,
      startDateTimeUtc: mission.data.startDateTimeUtc,
      missionNamePam: formatMissionName(mission.data.startDateTimeUtc),
      missionNameUlam: formatMissionNameUlam(mission.data.startDateTimeUtc),
      endDateTimeUtcText: formatDateForFrenchHumans(mission.data.endDateTimeUtc),
      startDateTimeUtcText: formatDateForFrenchHumans(mission.data.startDateTimeUtc),
      resources: mission.generalInfos.resources,
      missionReportType: mission.generalInfos.missionReportType,
      isUnderJdp: mission.data.isUnderJdp,
      jdpType: mission.generalInfos.jdpType
    }
  }

  return {
    getMissionListItem
  }
}
