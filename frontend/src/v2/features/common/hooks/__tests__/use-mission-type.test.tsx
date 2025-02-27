import { renderHook } from '@testing-library/react'
import { MissionReinforcementTypeEnum, MissionReportTypeEnum, MissionType } from '../../types/mission-types'
import { useMissionType } from '../use-mission-type'

describe('useMissionType', () => {
  it('should return mission type options', () => {
    const { result } = renderHook(() => useMissionType())
    expect(result.current.missionTypeOptions.length).toEqual(3)
  })

  it('should return reinforcement type options', () => {
    const { result } = renderHook(() => useMissionType())
    expect(result.current.reinforcementTypeOptions.length).toEqual(6)
  })

  it('should return report type options', () => {
    const { result } = renderHook(() => useMissionType())
    expect(result.current.reportTypeOptions.length).toEqual(3)
  })

  it('should return all mission type  name', () => {
    const { result } = renderHook(() => useMissionType())
    Object.keys(MissionType).forEach(type => {
      expect(result.current.getMissionTypeLabel(type as MissionType)).toBeDefined()
    })
  })

  it('should return all reinforcement type  name', () => {
    const { result } = renderHook(() => useMissionType())
    Object.keys(MissionReinforcementTypeEnum).forEach(type => {
      expect(result.current.getReinforcementTypeLabel(type as MissionReinforcementTypeEnum)).toBeDefined()
    })
  })

  it('should return all report type  name', () => {
    const { result } = renderHook(() => useMissionType())
    Object.keys(MissionReportTypeEnum).forEach(type => {
      expect(result.current.getReportTypeLabel(type as MissionReportTypeEnum)).toBeDefined()
    })
  })
})
