import { renderHook } from '@testing-library/react'
import { useMissionTag } from '../use-mission-tag.tsx'
import { MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { THEME } from '@mtes-mct/monitor-ui'

describe('useMissionTag with RAPPORT_NAV as MissionSource', () => {
  it("Should return Ouverte par l'unité, background gunMetal and textColor white", () => {
    const { result  } = renderHook(() => useMissionTag(MissionSourceEnum.RAPPORT_NAV))
    expect(result.current.getTagTextContent()).toBe('Ouverte par l\'unité')
    expect(result.current.getTagBackgroundColor()).toBe(THEME.color.gunMetal)
    expect(result.current.getTagTextColor()).toBe(THEME.color.white)
  })
})

describe('useMissionTag with MONITORENV as MissionSource', () => {
  it("Should return Ouverte par le CACEM, background mediumSeaGreen and textColor white", () => {
    const { result  } = renderHook(() => useMissionTag(MissionSourceEnum.MONITORENV))
    expect(result.current.getTagTextContent()).toBe('Ouverte par le CACEM')
    expect(result.current.getTagBackgroundColor()).toBe(THEME.color.mediumSeaGreen)
    expect(result.current.getTagTextColor()).toBe(THEME.color.white)
  })
})

describe('useMissionTag with MONITORFISH as MissionSource', () => {
  it("Should return Ouverte par le CNSP, background mediumSeaGreen and textColor white", () => {
    const { result  } = renderHook(() => useMissionTag(MissionSourceEnum.MONITORFISH))
    expect(result.current.getTagTextContent()).toBe('Ouverte par le CNSP')
    expect(result.current.getTagBackgroundColor()).toBe(THEME.color.blueGray)
    expect(result.current.getTagTextColor()).toBe(THEME.color.white)
  })
})

describe('useMissionTag with POSEIDON_CACEM as MissionSource', () => {
  it("Should return Ouverte par le CACEM, background mediumSeaGreen and textColor white", () => {
    const { result  } = renderHook(() => useMissionTag(MissionSourceEnum.POSEIDON_CACEM))
    expect(result.current.getTagTextContent()).toBe('Ouverte par le CACEM')
    expect(result.current.getTagBackgroundColor()).toBe(THEME.color.mediumSeaGreen)
    expect(result.current.getTagTextColor()).toBe(THEME.color.white)
  })
})

describe('useMissionTag with POSEIDON_CNSP as MissionSource', () => {
  it("Should return Ouverte par le CNSP, background blueGray and textColor white", () => {
    const { result  } = renderHook(() => useMissionTag(MissionSourceEnum.POSEIDON_CNSP))
    expect(result.current.getTagTextContent()).toBe('Ouverte par le CNSP')
    expect(result.current.getTagBackgroundColor()).toBe(THEME.color.blueGray)
    expect(result.current.getTagTextColor()).toBe(THEME.color.white)
  })
})
