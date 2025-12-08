import { render, screen } from '../../../../../../test-utils'
import MissionControlNavSummary from '../mission-control-nav-summary'
import { VesselTypeEnum } from '@common/types/mission-types.ts'
import { ControlMethod } from '@common/types/control-types.ts'

describe('MissionControlNavSummary', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionControlNavSummary vesselType={VesselTypeEnum.SAILING} controlMethod={ControlMethod.SEA} />
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should render properly', () => {
    render(<MissionControlNavSummary vesselType={VesselTypeEnum.SAILING} controlMethod={ControlMethod.SEA} />)
    expect(screen.getByText('Type de contrôle:', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('en Mer', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('Type de cible:', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('Navire de plaisance professionnelle', { exact: false })).toBeInTheDocument()
  })

  it('should render empty when undefined props', () => {
    render(<MissionControlNavSummary vesselType={undefined} controlMethod={undefined} />)
    expect(screen.getByText('Type de contrôle:', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('Type de cible:', { exact: false })).toBeInTheDocument()
  })
})
