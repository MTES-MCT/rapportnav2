import { MissionActionType } from '@common/types/fish-mission-types.ts'
import { describe, expect, it } from 'vitest'
import { ActionType } from '../../../common/types/action-type.ts'
import { MissionAction } from '../../../common/types/mission-action.ts'
import { MissionSourceEnum } from '../../../common/types/mission-types.ts'
import { hasSatiPdf } from '../use-sati.tsx'

const action = (source: MissionSourceEnum, actionType: ActionType, fishActionType?: MissionActionType) =>
  ({ source, actionType, data: { fishActionType } }) as unknown as MissionAction

describe('hasSatiPdf', () => {
  it('is true for a fish sea control (SATI module M1)', () => {
    expect(hasSatiPdf(action(MissionSourceEnum.MONITORFISH, ActionType.CONTROL, MissionActionType.SEA_CONTROL))).toBe(
      true
    )
  })

  it.each([MissionActionType.LAND_CONTROL, MissionActionType.AIR_CONTROL])(
    'is false for a fish %s, which has no SATI pdf yet',
    fishActionType => {
      expect(hasSatiPdf(action(MissionSourceEnum.MONITORFISH, ActionType.CONTROL, fishActionType))).toBe(false)
    }
  )

  it.each([
    [MissionSourceEnum.MONITORENV, ActionType.CONTROL],
    [MissionSourceEnum.RAPPORT_NAV, ActionType.CONTROL],
    [MissionSourceEnum.MONITORFISH, ActionType.SURVEILLANCE]
  ])('is false for %s / %s', (source, actionType) => {
    expect(hasSatiPdf(action(source, actionType, MissionActionType.SEA_CONTROL))).toBe(false)
  })
})
