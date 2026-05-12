import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { ActionFishControlInput } from '../../../types/action-type'
import FishControlConclusion from '../fish-control-conclusion'

vi.mock('../mission-action-incident-download', () => ({
  default: () => <div data-testid="incident-download" />
}))

vi.mock('../mission-action-diving-operation', () => ({
  default: () => <div data-testid="diving-operation" />
}))

vi.mock('../../../../mission-infraction/components/ui/mission-infraction-fish-summary', () => ({
  default: ({ infractions }: { infractions: { natinf: number; threat: string }[] }) => (
    <div data-testid="infraction-fish-summary">
      {infractions.map((inf, i) => (
        <div key={i}>{inf.threat}</div>
      ))}
    </div>
  )
}))

vi.mock('../../../../mission-target/components/elements/mission-target-item-default', () => ({
  default: () => <div data-testid="target-item" />
}))

vi.mock('../../../../mission-target/hooks/use-target', () => ({
  useTarget: () => ({ defaultControlTypes: [], getAvailableControlTypes: vi.fn() })
}))

const defaultValues = {
  fishActionType: 'SEA_CONTROL',
  faoAreas: [],
  userTrigram: 'ABC',
  fishInfractions: [
    {
      natinf: 12345,
      threat: 'Dissimulation'
    },
    {
      natinf: 22182,
      threat: 'Entrave à la justice'
    }
  ]
} as unknown as ActionFishControlInput

describe('FishControlConclusion', () => {
  it('renders the conclusions label', () => {
    render(<FishControlConclusion values={defaultValues} />)
    expect(screen.getByText('Conclusions')).toBeInTheDocument()
  })

  it('renders the infraction fish summary section', () => {
    render(<FishControlConclusion values={defaultValues} />)
    expect(screen.getByTestId('infraction-fish-summary')).toBeInTheDocument()
  })

  it('renders the observations textarea', () => {
    render(<FishControlConclusion values={defaultValues} />)
    expect(screen.getByTestId('observations-by-unit')).toBeInTheDocument()
  })

  it.skip('renders the incident download component', () => {
    //TODO: remove skip when MonitorFish dev is done
    render(<FishControlConclusion values={defaultValues} />)
    expect(screen.getByTestId('incident-download')).toBeInTheDocument()
  })

  it.skip('renders the diving operation component', () => {
    //TODO: remove skip when MonitorFish dev is done
    render(<FishControlConclusion values={defaultValues} />)
    expect(screen.getByTestId('diving-operation')).toBeInTheDocument()
  })

  it('renders the target item', () => {
    render(<FishControlConclusion values={defaultValues} />, {
      formikValues: { targets: [{}] }
    })
    expect(screen.getByTestId('target-item')).toBeInTheDocument()
  })

  it('renders fish infractions with natinfs', () => {
    render(<FishControlConclusion values={defaultValues} />)
    expect(screen.getByText('Infraction 1 : Dissimulation', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('Entrave à la justice', { exact: false })).toBeInTheDocument()
  })
})
