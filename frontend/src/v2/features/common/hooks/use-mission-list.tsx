import { MissionListData, MissionListItem } from '../types/mission-types'
import { useDate } from './use-date'
import { useMissionTag } from './use-mission-tag'

interface MissionListHook {
  getMissionListItem: (mission: MissionListData) => MissionListItem
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
  const getCrewNumber = (mission: MissionListData) =>
    !mission?.serviceId ? '--' : mission.serviceId % 2 === 0 ? 'B' : 'A'

  const getExportLabel = (mission: MissionListData) =>
    `- ${formatMissionName(mission.startDateTimeUtc)} - ${getOpenByText(mission.missionSource)} - ${mission.actionCount ?? 0} action(s)`

  const getMissionListItem = (mission: MissionListData): MissionListItem => {
    return {
      id: mission.id,
      status: mission.status,
      idUUID: mission.idUUID,
      openBy: mission.openBy,
      crew: mission.crew,
      crewNumber: getCrewNumber(mission),
      exportLabel: getExportLabel(mission),
      controlUnits: mission.controlUnits,
      missionSource: mission.missionSource,
      completenessForStats: mission.completenessForStats,
      observationsByUnit: mission.observationsByUnit,
      endDateTimeUtc: mission.endDateTimeUtc,
      startDateTimeUtc: mission.startDateTimeUtc,
      missionNamePam: formatMissionName(mission.startDateTimeUtc),
      missionNameUlam: formatMissionNameUlam(mission.startDateTimeUtc),
      endDateTimeUtcText: formatDateForFrenchHumans(mission.endDateTimeUtc),
      startDateTimeUtcText: formatDateForFrenchHumans(mission.startDateTimeUtc),
      resources: mission.resources,
      missionReportType: mission.missionReportType,
      isUnderJdp: mission.isUnderJdp,
      jdpType: mission.jdpType,
      isResourcesNotUsed: mission.isResourcesNotUsed
    }
  }

  return {
    getMissionListItem
  }
}
