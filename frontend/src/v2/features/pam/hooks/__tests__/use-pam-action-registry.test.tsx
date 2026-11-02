import { Icon } from '@mtes-mct/monitor-ui'
import { vi } from 'vitest'
import { render } from '../../../../../test-utils'
import { ActionType } from '../../../common/types/action-type'
import { MissionAction } from '../../../common/types/mission-action'
import { ModuleType } from '../../../common/types/module-type'
import { useUlamActionRegistry } from '../../../ulam/hooks/use-ulam-action-registry'
import { usePamActionRegistry } from '../use-pam-action-registry'

const trainingProps = vi.hoisted(() => vi.fn())

vi.mock('../../../mission-action/components/elements/mission-action-item-unit-management-training', () => ({
  default: (props: unknown) => {
    trainingProps(props)
    return null
  }
}))

const action = { id: 'action-id', actionType: ActionType.UNIT_MANAGEMENT_TRAINING } as MissionAction

describe('UNIT_MANAGEMENT_TRAINING registry entries', () => {
  beforeEach(() => vi.clearAllMocks())

  it('should render the training form with the PAM module type in PAM', () => {
    const { title, icon, component } = usePamActionRegistry(ActionType.UNIT_MANAGEMENT_TRAINING)
    expect(title).toBe('Entraînement')
    expect(icon).toBe(Icon.GroupPerson)
    const Component = component!

    render(<Component action={action} onChange={vi.fn()} />)
    expect(trainingProps.mock.calls[0][0].moduleType).toBe(ModuleType.PAM)
  })

  it('should render the training form without a module type in ULAM', () => {
    const { title, icon, component } = useUlamActionRegistry(ActionType.UNIT_MANAGEMENT_TRAINING)
    expect(title).toBe('Entraînement')
    expect(icon).toBe(Icon.GroupPerson)
    const Component = component!

    render(<Component action={action} onChange={vi.fn()} />)
    expect(trainingProps.mock.calls[0][0].moduleType).toBeUndefined()
  })
})
