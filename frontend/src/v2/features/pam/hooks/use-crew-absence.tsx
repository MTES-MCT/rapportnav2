import { MissionCrew } from '../../common/types/crew-type.ts'

export const useCrewAbsence = () => {
  function countFullMissionAbsences(crew: MissionCrew[]): number {
    return crew.reduce(
      (count, member) => count + ((member.absences ?? []).some(a => a.isAbsentFullMission) ? 1 : 0),
      0
    )
  }

  function getFullMissionAbsenceText(crew: MissionCrew[]): string {
    const count = countFullMissionAbsences(crew)
    return count <= 1 ? `${count} membre non embarqué` : `${count} membres non embarqués`
  }

  return {
    countFullMissionAbsences,
    getFullMissionAbsenceText
  }
}
