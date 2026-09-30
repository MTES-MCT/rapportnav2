import { render, screen } from '../../../../../../test-utils.tsx'
import { MissionActionSummary, MissionSourceEnum } from '../../../../common/types/mission-types.ts'
import MissionActionRecapTags from '../mission-action-recap-tags.tsx'

describe('MissionActionRecapTags component', () => {
  test('renders nothing when there is no summary', () => {
    const { container } = render(<MissionActionRecapTags actionsSummary={undefined} />)
    expect(container).toBeEmptyDOMElement()
  })

  test('MonitorFish shows controls only (with plural), never surveillances or autres', () => {
    const summary: MissionActionSummary = {
      source: MissionSourceEnum.MONITORFISH,
      nbControls: 2,
      nbSurveillances: 3,
      nbOtherActions: 4
    }
    render(<MissionActionRecapTags actionsSummary={[summary]} />)

    expect(screen.getByText('2 contrôles')).toBeInTheDocument()
    expect(screen.queryByText(/surveillance/)).not.toBeInTheDocument()
    expect(screen.queryByText(/autre/)).not.toBeInTheDocument()
  })

  test('MonitorEnv shows controls and surveillances but not autres', () => {
    const summary: MissionActionSummary = {
      source: MissionSourceEnum.MONITORENV,
      nbControls: 1,
      nbSurveillances: 2,
      nbOtherActions: 5
    }
    render(<MissionActionRecapTags actionsSummary={[summary]} />)

    expect(screen.getByText('1 contrôle')).toBeInTheDocument()
    expect(screen.getByText('2 surveillances')).toBeInTheDocument()
    expect(screen.queryByText(/autre/)).not.toBeInTheDocument()
  })

  test('RapportNav shows controls, surveillances and autres, skipping zero counts', () => {
    const summary: MissionActionSummary = {
      source: MissionSourceEnum.RAPPORT_NAV,
      nbControls: 3,
      nbSurveillances: 0,
      nbOtherActions: 1
    }
    render(<MissionActionRecapTags actionsSummary={[summary]} />)

    expect(screen.getByText('3 contrôles')).toBeInTheDocument()
    expect(screen.getByText('1 autre action')).toBeInTheDocument()
    expect(screen.queryByText(/surveillance/)).not.toBeInTheDocument()
  })

  test('uses singular labels at count 1 for surveillances and autres', () => {
    const summary: MissionActionSummary = {
      source: MissionSourceEnum.RAPPORT_NAV,
      nbControls: 1,
      nbSurveillances: 1,
      nbOtherActions: 1
    }
    render(<MissionActionRecapTags actionsSummary={[summary]} />)

    expect(screen.getByText('1 contrôle')).toBeInTheDocument()
    expect(screen.getByText('1 surveillance')).toBeInTheDocument()
    expect(screen.getByText('1 autre action')).toBeInTheDocument()
  })

  test('renders tags for several sources together', () => {
    const summaries: MissionActionSummary[] = [
      { source: MissionSourceEnum.RAPPORT_NAV, nbControls: 2, nbSurveillances: 1, nbOtherActions: 3 },
      { source: MissionSourceEnum.MONITORENV, nbControls: 1, nbSurveillances: 2, nbOtherActions: 0 },
      { source: MissionSourceEnum.MONITORFISH, nbControls: 4, nbSurveillances: 0, nbOtherActions: 0 }
    ]
    render(<MissionActionRecapTags actionsSummary={summaries} />)

    // RapportNav: controls + surveillances + autres
    expect(screen.getByText('2 contrôles')).toBeInTheDocument()
    expect(screen.getByText('1 surveillance')).toBeInTheDocument()
    expect(screen.getByText('3 autres actions')).toBeInTheDocument()
    // MonitorEnv: controls + surveillances
    expect(screen.getByText('1 contrôle')).toBeInTheDocument()
    expect(screen.getByText('2 surveillances')).toBeInTheDocument()
    // MonitorFish: controls only
    expect(screen.getByText('4 contrôles')).toBeInTheDocument()
  })

  test('renders nothing for a source whose displayed counts are all zero', () => {
    const summary: MissionActionSummary = {
      source: MissionSourceEnum.MONITORFISH,
      nbControls: 0,
      nbSurveillances: 5,
      nbOtherActions: 5
    }
    render(<MissionActionRecapTags actionsSummary={[summary]} />)

    expect(screen.queryByText(/contrôle/)).not.toBeInTheDocument()
    expect(screen.queryByText(/surveillance/)).not.toBeInTheDocument()
  })
})
