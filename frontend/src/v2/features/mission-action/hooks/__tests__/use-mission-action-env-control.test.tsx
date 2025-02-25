import { ControlType } from '@common/types/control-types'
import { renderHook } from '@testing-library/react'
import { MissionAction } from 'src/v2/features/common/types/mission-action'
import { vi } from 'vitest'
import { ActionEnvControlInput } from '../../types/action-type'
import { useMissionActionEnvControl } from '../use-mission-action-env-control'

const onChange = vi.fn()

describe('useMissionActionEnvControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should calcul av', () => {
    const action = {} as MissionAction
    const { result } = renderHook(() => useMissionActionEnvControl(action, onChange, false))
    const value = {
      controlSecurity: {
        amountOfControls: 0
      },
      controlGensDeMer: {
        amountOfControls: 1
      },
      controlNavigation: {
        amountOfControls: 1
      },
      controlAdministrative: {
        amountOfControls: 0
      },
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
