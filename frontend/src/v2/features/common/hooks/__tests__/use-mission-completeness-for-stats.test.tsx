import { Icon, Level, THEME } from '@mtes-mct/monitor-ui'
import { renderHook } from '@testing-library/react'
import { CompletenessForStatsStatusEnum, MissionSourceEnum } from '../../types/mission-types'
import { useMissionCompletenessForStats } from '../use-mission-completeness-for-stats'

describe('useMissionCompletenessForStats', () => {
  it('should return stats for mission ended and complete', () => {
    const isMissionFinished = true
    const completeForStats = {
      sources: [],
      status: CompletenessForStatsStatusEnum.COMPLETE
    }

    const { result } = renderHook(() => useMissionCompletenessForStats(completeForStats, isMissionFinished))

    expect(result.current.icon).toEqual(Icon.Confirm)
    expect(result.current.text).toEqual('Complété')
    expect(result.current.bannerLevel).toEqual(Level.SUCCESS)
    expect(result.current.color).toEqual(THEME.color.mediumSeaGreen)
    expect(result.current.bannerMessage).toEqual(
      `La mission est terminée et ses données sont complètes : le rapport est comptabilisé dans les statistiques. Vous pouvez désormais exporter le rapport de mission.`
    )
  })

  it('should return stats for mission ended and not complete', () => {
    const isMissionFinished = true
    const completeForStats = {
      sources: [],
      status: CompletenessForStatsStatusEnum.INCOMPLETE
    }
    const { result } = renderHook(() => useMissionCompletenessForStats(completeForStats, isMissionFinished))
    expect(result.current.icon).toEqual(Icon.AttentionFilled)
    expect(result.current.text).toEqual('À compléter')
    expect(result.current.color).toEqual(THEME.color.maximumRed)
  })

  it('should return stats for mission not ended and complete', () => {
    const isMissionFinished = false
    const completeForStats = {
      sources: [],
      status: CompletenessForStatsStatusEnum.COMPLETE
    }
    const { result } = renderHook(() => useMissionCompletenessForStats(completeForStats, isMissionFinished))
    expect(result.current.icon).toEqual(Icon.Confirm)
    expect(result.current.text).toEqual('Données à jour')
    expect(result.current.color).toEqual(THEME.color.mediumSeaGreen)
  })

  it('should return stats for mission not ended and not complete', () => {
    const isMissionFinished = false
    const completeForStats = {
      sources: [],
      status: CompletenessForStatsStatusEnum.INCOMPLETE
    }
    const { result } = renderHook(() => useMissionCompletenessForStats(completeForStats, isMissionFinished))
    expect(result.current.icon).toEqual(Icon.AttentionFilled)
    expect(result.current.text).toEqual('À compléter')
    expect(result.current.color).toEqual(THEME.color.charcoal)
  })

  it('should banner message regarding sources CNSP and CACEM', () => {
    const isMissionFinished = false
    const completeForStats = {
      sources: [MissionSourceEnum.MONITORENV, MissionSourceEnum.MONITORFISH],
      status: CompletenessForStatsStatusEnum.INCOMPLETE
    }
    const { result } = renderHook(() => useMissionCompletenessForStats(completeForStats, isMissionFinished))
    expect(result.current.bannerMessage).toContain(`le CNSP et le CACEM`)
  })

  it('should banner message regarding sources unité', () => {
    const isMissionFinished = false
    const completeForStats = {
      sources: [MissionSourceEnum.RAPPORT_NAV],
      status: CompletenessForStatsStatusEnum.INCOMPLETE
    }
    const { result } = renderHook(() => useMissionCompletenessForStats(completeForStats, isMissionFinished))
    expect(result.current.bannerMessage).toContain(`votre unité`)
  })
})
