import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { ActionType } from '../../../../common/types/action-type'
import { MissionAction } from '../../../../common/types/mission-action'
import MissionActionItemRoadsideControl from '../mission-action-item-control-roadside'

const genericControlProps = vi.hoisted(() => vi.fn())

vi.mock('../mission-action-item-generic-control.tsx', () => ({
  default: (props: unknown) => {
    genericControlProps(props)
    return <div data-testid="generic-control" />
  }
}))

const action = { id: 'action-id', ownerId: 'mission-id', actionType: ActionType.CONTROL_ROADSIDE } as MissionAction

describe('MissionActionItemRoadsideControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should render the generic control with its own test id', () => {
    const onChange = vi.fn()
    render(<MissionActionItemRoadsideControl action={action} onChange={onChange} />)

    const props = genericControlProps.mock.calls[0][0]
    expect(props.action).toBe(action)
    expect(props.onChange).toBe(onChange)
    expect(props.withGeoCoords).toBe(false)
    expect(props['data-testid']).toBe('action-control-roadside')
  })

  it('should not define roadside-specific form nor schema yet', () => {
    render(<MissionActionItemRoadsideControl action={action} onChange={vi.fn()} />)

    const props = genericControlProps.mock.calls[0][0]
    expect(props.component).toBeUndefined()
    expect(props.schema).toBeUndefined()
  })
})
