import { vi } from 'vitest'
import { object, ValidationError } from 'yup'
import { render } from '../../../../../../test-utils'
import { ActionType } from '../../../../common/types/action-type'
import { MissionAction } from '../../../../common/types/mission-action'
import { useMissionFinished } from '../../../../common/hooks/use-mission-finished.tsx'
import MissionActionItemSectorPlaisanceControlForm from '../../ui/mission-action-control-sector-plaisance-form'
import MissionActionItemSectorPlaisanceControl from '../mission-action-item-control-sector-plaisance'

const genericControlProps = vi.hoisted(() => vi.fn())

vi.mock('../mission-action-item-generic-control.tsx', () => ({
  default: (props: unknown) => {
    genericControlProps(props)
    return <div data-testid="generic-control" />
  }
}))
vi.mock('../../../../common/hooks/use-mission-finished.tsx', () => ({ useMissionFinished: vi.fn() }))

const action = {
  id: 'action-id',
  ownerId: 'mission-id',
  actionType: ActionType.CONTROL_SECTOR_PLAISANCE
} as MissionAction

describe('MissionActionItemSectorPlaisanceControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useMissionFinished).mockReturnValue(false)
  })

  it('should render the generic control with its own test id and the plaisance form', () => {
    const onChange = vi.fn()
    render(<MissionActionItemSectorPlaisanceControl action={action} onChange={onChange} />)

    const props = genericControlProps.mock.calls[0][0]
    expect(props.action).toBe(action)
    expect(props.onChange).toBe(onChange)
    expect(props.withGeoCoords).toBe(false)
    expect(props['data-testid']).toBe('action-control-sector-plaisance')
    expect(props.component).toBe(MissionActionItemSectorPlaisanceControlForm)
  })

  it('should pass the plaisance schema, without sectorType nor the fishing-only fields', () => {
    render(<MissionActionItemSectorPlaisanceControl action={action} onChange={vi.fn()} />)

    const { schema } = genericControlProps.mock.calls[0][0]
    expect(Object.keys(schema)).toEqual(['sectorEstablishmentType', 'establishment'])
  })

  it('should require the establishment type only once the mission is finished', async () => {
    render(<MissionActionItemSectorPlaisanceControl action={action} onChange={vi.fn()} />)
    await expect(object(genericControlProps.mock.calls[0][0].schema).validate({})).resolves.toBeDefined()

    vi.mocked(useMissionFinished).mockReturnValue(true)
    render(<MissionActionItemSectorPlaisanceControl action={action} onChange={vi.fn()} />)
    const finishedSchema = object(genericControlProps.mock.calls[1][0].schema)
    const error = await finishedSchema.validate({}, { abortEarly: false }).catch((e: ValidationError) => e)
    expect((error as ValidationError).inner.map(e => e.path)).toContain('sectorEstablishmentType')
  })
})
