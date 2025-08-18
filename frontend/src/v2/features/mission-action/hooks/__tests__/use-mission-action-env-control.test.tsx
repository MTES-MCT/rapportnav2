import { ControlType } from '@common/types/control-types'
import { InfractionTypeEnum } from '@common/types/env-mission-types'
import { vi } from 'vitest'
import { renderHook } from '../../../../../test-utils'
import { MissionAction } from '../../../common/types/mission-action'
import { ActionEnvControlInput } from '../../types/action-type'
import { useMissionActionEnvControl } from '../use-mission-action-env-control'

const onChange = vi.fn()

describe('useMissionActionEnvControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should calcul availables control types', () => {
    const action = {} as MissionAction
    const { result } = renderHook(() => useMissionActionEnvControl(action, onChange, false))
    const value = {
      targets: [
        {
          controls: [
            {
              amountOfControls: 0,
              controlType: ControlType.SECURITY
            },
            {
              amountOfControls: 1,
              controlType: ControlType.GENS_DE_MER
            },
            {
              amountOfControls: 1,
              controlType: ControlType.NAVIGATION
            },
            {
              amountOfControls: 1,
              controlType: ControlType.ADMINISTRATIVE
            }
          ]
        }
      ],
      availableControlTypesForInfraction: [
        ControlType.SECURITY,
        ControlType.NAVIGATION,
        ControlType.GENS_DE_MER,
        ControlType.ADMINISTRATIVE
      ]
    } as ActionEnvControlInput
    const responses = result.current.getAvailableControlTypes(value)

    expect(responses).toHaveLength(3)
    expect(responses).toContain(ControlType.NAVIGATION)
    expect(responses).toContain(ControlType.GENS_DE_MER)
    expect(responses).toContain(ControlType.ADMINISTRATIVE)
  })

  it('should calcul availables control types except those already on target', () => {
    const action = {} as MissionAction
    const { result } = renderHook(() => useMissionActionEnvControl(action, onChange, false))
    const value = {
      targets: [
        {
          controls: [
            {
              amountOfControls: 0,
              controlType: ControlType.SECURITY
            },
            {
              amountOfControls: 1,
              controlType: ControlType.GENS_DE_MER
            },
            {
              amountOfControls: 1,
              controlType: ControlType.NAVIGATION
            },
            {
              amountOfControls: 1,
              infractions: [
                {
                  id: 'erere',
                  infractionType: InfractionTypeEnum.WITH_REPORT
                }
              ],
              controlType: ControlType.ADMINISTRATIVE
            }
          ]
        }
      ],
      availableControlTypesForInfraction: [
        ControlType.SECURITY,
        ControlType.NAVIGATION,
        ControlType.GENS_DE_MER,
        ControlType.ADMINISTRATIVE
      ]
    } as ActionEnvControlInput
    const responses = result.current.getAvailableControlTypes(value)

    expect(responses).toHaveLength(2)
    expect(responses).toContain(ControlType.NAVIGATION)
    expect(responses).toContain(ControlType.GENS_DE_MER)
  })
})
