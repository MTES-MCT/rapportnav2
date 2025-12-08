import { describe, it, expect } from 'vitest'
import { renderHook } from '@testing-library/react'
import { useCrewAbsence } from '../use-crew-absence.tsx'
import { MissionCrew, MissionCrewAbsenceReason } from '../../../common/types/crew-type.ts'

const createCrew = (absences: MissionCrew['absences'] = []): MissionCrew => ({
  id: '1',
  absences
})

describe('useCrewAbsence', () => {
  describe('countFullMissionAbsences', () => {
    it('should return 0 for an empty crew list', () => {
      const { result } = renderHook(() => useCrewAbsence())
      expect(result.current.countFullMissionAbsences([])).toBe(0)
    })

    it('should return 0 when no crew member has a full mission absence', () => {
      const { result } = renderHook(() => useCrewAbsence())
      const crew: MissionCrew[] = [
        createCrew([{ isAbsentFullMission: false, reason: MissionCrewAbsenceReason.SICK_LEAVE }]),
        createCrew([{ isAbsentFullMission: false }]),
        createCrew([])
      ]
      expect(result.current.countFullMissionAbsences(crew)).toBe(0)
    })

    it('should count crew members with a full mission absence', () => {
      const { result } = renderHook(() => useCrewAbsence())
      const crew: MissionCrew[] = [
        createCrew([{ isAbsentFullMission: true, reason: MissionCrewAbsenceReason.SICK_LEAVE }]),
        createCrew([{ isAbsentFullMission: false }]),
        createCrew([{ isAbsentFullMission: true, reason: MissionCrewAbsenceReason.HOLIDAYS }])
      ]
      expect(result.current.countFullMissionAbsences(crew)).toBe(2)
    })

    it('should count a crew member only once even with multiple full mission absences', () => {
      const { result } = renderHook(() => useCrewAbsence())
      const crew: MissionCrew[] = [
        createCrew([
          { isAbsentFullMission: true, reason: MissionCrewAbsenceReason.SICK_LEAVE },
          { isAbsentFullMission: true, reason: MissionCrewAbsenceReason.TRAINING }
        ])
      ]
      expect(result.current.countFullMissionAbsences(crew)).toBe(1)
    })
  })

  describe('getFullMissionAbsenceText', () => {
    it('should return singular text for 0 absences', () => {
      const { result } = renderHook(() => useCrewAbsence())
      expect(result.current.getFullMissionAbsenceText([])).toBe('0 membre non embarqué')
    })

    it('should return singular text for 1 absence', () => {
      const { result } = renderHook(() => useCrewAbsence())
      const crew: MissionCrew[] = [
        createCrew([{ isAbsentFullMission: true, reason: MissionCrewAbsenceReason.SICK_LEAVE }])
      ]
      expect(result.current.getFullMissionAbsenceText(crew)).toBe('1 membre non embarqué')
    })

    it('should return plural text for 2 or more absences', () => {
      const { result } = renderHook(() => useCrewAbsence())
      const crew: MissionCrew[] = [
        createCrew([{ isAbsentFullMission: true, reason: MissionCrewAbsenceReason.SICK_LEAVE }]),
        createCrew([{ isAbsentFullMission: true, reason: MissionCrewAbsenceReason.HOLIDAYS }])
      ]
      expect(result.current.getFullMissionAbsenceText(crew)).toBe('2 membres non embarqués')
    })
  })
})
