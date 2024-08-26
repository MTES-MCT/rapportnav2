import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import InfractionSummary from './infraction-summary.tsx'
import { ControlType } from '../../../../../common/types/control-types.ts'
import { InfractionTypeEnum } from '../../../../../common/types/env-mission-types.ts'

const infraction = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}

const props = (infractions = [infraction]) => ({
  controlType: ControlType.ADMINISTRATIVE,
  infractions,
  onEdit: vi.fn(),
  onDelete: vi.fn()
})
describe('InfractionSummary', () => {
  test('renders the component', async () => {
    render(<InfractionSummary {...props()} />)
    expect(screen.getByText('Infraction administrative')).toBeInTheDocument()
  })
  test('calls onDelete when clicking the delete button', async () => {
    const onDelete = vi.fn()
    render(<InfractionSummary {...{ ...props(), onDelete }} />)
    const button = screen.getByRole('delete-infraction')
    fireEvent.click(button)
    expect(onDelete).toHaveBeenCalled()
  })
})
