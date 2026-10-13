import { vi } from 'vitest'
import { object, ValidationError } from 'yup'
import { render } from '../../../../../../test-utils'
import { ActionType } from '../../../../common/types/action-type'
import { MissionAction } from '../../../../common/types/mission-action'
import { useMissionFinished } from '../../../../common/hooks/use-mission-finished.tsx'
import MissionActionItemSectorFishingControlForm from '../../ui/mission-action-control-sector-fishing-form'
import MissionActionItemSectorFishingControl from '../mission-action-item-control-sector-fishing'

const genericControlProps = vi.hoisted(() => vi.fn())

vi.mock('../mission-action-item-generic-control.tsx', () => ({
  default: (props: unknown) => {
    genericControlProps(props)
    return <div data-testid="generic-control" />
  }
}))
vi.mock('../../../../common/hooks/use-mission-finished.tsx', () => ({ useMissionFinished: vi.fn() }))

const action = { id: 'action-id', ownerId: 'mission-id', actionType: ActionType.CONTROL_SECTOR_FISHING } as MissionAction

describe('MissionActionItemSectorFishingControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useMissionFinished).mockReturnValue(false)
  })

  it('should render the generic control with its own test id and the fishing form', () => {
    const onChange = vi.fn()
    render(<MissionActionItemSectorFishingControl action={action} onChange={onChange} />)

    const props = genericControlProps.mock.calls[0][0]
    expect(props.action).toBe(action)
    expect(props.onChange).toBe(onChange)
    expect(props.withGeoCoords).toBe(false)
    expect(props['data-testid']).toBe('action-control-sector-fishing')
    expect(props.component).toBe(MissionActionItemSectorFishingControlForm)
  })

  it('should pass the fishing schema, without sectorType', () => {
    render(<MissionActionItemSectorFishingControl action={action} onChange={vi.fn()} />)

    const { schema } = genericControlProps.mock.calls[0][0]
    expect(Object.keys(schema)).toEqual(['sectorEstablishmentType', 'establishment', 'portLocode', 'fishAuction'])
  })

  it('should require the establishment type only once the mission is finished', async () => {
    render(<MissionActionItemSectorFishingControl action={action} onChange={vi.fn()} />)
    await expect(object(genericControlProps.mock.calls[0][0].schema).validate({})).resolves.toBeDefined()

    vi.mocked(useMissionFinished).mockReturnValue(true)
    render(<MissionActionItemSectorFishingControl action={action} onChange={vi.fn()} />)
    const finishedSchema = object(genericControlProps.mock.calls[1][0].schema)
    const error = await finishedSchema.validate({}, { abortEarly: false }).catch((e: ValidationError) => e)
    expect((error as ValidationError).inner.map(e => e.path)).toContain('sectorEstablishmentType')
  })
})
