import { render, screen } from '../../../../../../test-utils'
import MissionNatinfTagFish from '../mission-natinfs-tag-fish'

describe('MissionNatinfTag', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <MissionNatinfTagFish
        natinf={12345}
        description={
          "Debarquement de produits de la peche maritime et de l'aquaculture marine sans notification prealable"
        }
      />
    )
    expect(wrapper).toMatchSnapshot()
  })

  it('should display no infraction and no description if not', () => {
    render(<MissionNatinfTagFish />)
    expect(screen.getByText('Sans infraction')).toBeInTheDocument()
    expect(screen.queryByTestId('fish-natinf-description')).toBeNull()
  })

  it('should display infraction and description', () => {
    render(
      <MissionNatinfTagFish
        natinf={12345}
        description={
          "Debarquement de produits de la peche maritime et de l'aquaculture marine sans notification prealable"
        }
      />
    )
    expect(screen.getByText('NATINF : 12345', { exact: false })).toBeInTheDocument()
    expect(
      screen.getByText(
        "Debarquement de produits de la peche maritime et de l'aquaculture marine sans notification prealable",
        { exact: false }
      )
    ).toBeInTheDocument()
  })
})
