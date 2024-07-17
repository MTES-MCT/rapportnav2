import { render, screen } from '../../../../test-utils.tsx'
import ActionOtherRescue from './timeline-item-rescue.tsx'

const actionMock = (isVesselRescue: boolean): any => ({
  id: '1',
  startDateTimeUtc: '2022-01-01T00:00:00Z',
  endDateTimeUtc: '2022-01-01T01:00:00Z',
  data: {
    isVesselRescue,
    isPersonRescue: !isVesselRescue
  }
})

const props = (action: any, onClick = vi.fn()) => ({
  action,
  onClick
})
describe('ActionOtherRescue', () => {
  test('should render Sauvegarde de la vie humaine', () => {
    render(<ActionOtherRescue {...props(actionMock(false))} />)
    expect(screen.getByText('Sauvegarde de la vie humaine', { exact: false })).toBeInTheDocument()
  })
  test('should render Assistance de navire en difficulté', () => {
    render(<ActionOtherRescue {...props(actionMock(true))} />)
    expect(screen.getByText('Assistance de navire en difficulté', { exact: false })).toBeInTheDocument()
  })
})
