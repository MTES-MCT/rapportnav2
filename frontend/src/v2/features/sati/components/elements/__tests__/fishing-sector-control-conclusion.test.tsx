import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { MissionActionData } from '../../../../common/types/mission-action-data'
import { Sati } from '../../../../common/types/sati'
import FishingSectorControlConclusion from '../fishing-sector-control-conclusion'

const layoutProps = vi.hoisted(() => vi.fn())

vi.mock('../../layout/control-conclusion-layout', () => ({
  default: (props: unknown) => {
    layoutProps(props)
    return <div data-testid="conclusion-layout" />
  }
}))

describe('FishingSectorControlConclusion', () => {
  beforeEach(() => vi.clearAllMocks())

  it('should render the conclusion layout with the sati of the values', () => {
    const sati = { actionId: 'action-id' } as Sati
    render(<FishingSectorControlConclusion values={{ sati } as MissionActionData} />)

    expect(screen.getByTestId('conclusion-layout')).toBeInTheDocument()
    expect(layoutProps.mock.calls[0][0].sati).toBe(sati)
  })

  it('should render the layout without sati when the values have none', () => {
    render(<FishingSectorControlConclusion values={{} as MissionActionData} />)

    expect(layoutProps.mock.calls[0][0].sati).toBeUndefined()
  })
})
