import { MissionTypeEnum } from '@common/types/env-mission-types'
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

  it('should if is reinforcement external type', () => {
    const { result } = renderHook(() => useMissionType())
    expect(result.current.isExternalReinforcementTime(MissionReportTypeEnum.FIELD_REPORT)).toBeFalsy()
    expect(
      result.current.isExternalReinforcementTime(MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT)
    ).toBeTruthy()
  })

  it('should if mission is type sea', () => {
    const { result } = renderHook(() => useMissionType())
    expect(result.current.isMissionTypeSea([MissionTypeEnum.AIR, MissionTypeEnum.LAND])).toBeFalsy()
    expect(
      result.current.isMissionTypeSea([MissionTypeEnum.AIR, MissionTypeEnum.LAND, MissionTypeEnum.SEA])
    ).toBeTruthy()
  })

  it('should if is mission report type is field', () => {
    const { result } = renderHook(() => useMissionType())
    expect(result.current.isEnvMission(MissionReportTypeEnum.OFFICE_REPORT)).toBeFalsy()
    expect(result.current.isEnvMission(MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT)).toBeFalsy()
    expect(
      result.current.isEnvMission(MissionReportTypeEnum.FIELD_REPORT)
    ).toBeTruthy()
  })
})
