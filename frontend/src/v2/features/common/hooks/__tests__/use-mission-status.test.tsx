import { renderHook } from '@testing-library/react'
import { MissionStatusEnum } from '../../types/mission-types'
import { useMissionStatus } from '../use-mission-status'

describe('useMissionStatus', () => {
  it('should return all mission status name', () => {
    Object.keys(MissionStatusEnum).forEach(status => {
      const { result } = renderHook(() => useMissionStatus(status as MissionStatusEnum))
      expect(result.current).toBeDefined()
    })
  })

  it('should return ended status name', () => {
    const { result } = renderHook(() => useMissionStatus(MissionStatusEnum.ENDED))
    expect(result.current.text).toEqual('Terminée')
  })

  it('should return in progress status name', () => {
    const { result } = renderHook(() => useMissionStatus(MissionStatusEnum.IN_PROGRESS))
    expect(result.current.text).toEqual('En cours')
  })

  it('should return unvaliable status name', () => {
    const { result } = renderHook(() => useMissionStatus(MissionStatusEnum.UNAVAILABLE))
    expect(result.current.text).toEqual('Indisponible')
  })

  it('should return upcoming status name', () => {
    const { result } = renderHook(() => useMissionStatus(MissionStatusEnum.UPCOMING))
    expect(result.current.text).toEqual('À venir')
  })
})
