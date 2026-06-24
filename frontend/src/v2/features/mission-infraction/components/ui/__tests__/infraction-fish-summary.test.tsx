import { render, screen } from '../../../../../../test-utils.tsx'
import InfractionFishSummary from '../infraction-fish-summary.tsx'

describe('InfractionFishSummary', () => {
  it('should display infraction, category, description and comment', () => {
    render(
      <InfractionFishSummary
        infractions={[
          {
            natinf: 12345,
            threat: 'Dissimulation',
            threatCharacterization:
              "Debarquement de produits de la pêche maritime et de l'aquaculture marine sans notification prealable",
            comments: 'commentaire'
          }
        ]}
      />
    )
    expect(screen.getByText('Infraction : Dissimulation')).toBeInTheDocument()
    expect(screen.getByText(/NATINF\s*:\s*12345/i)).toBeInTheDocument()
    expect(
      screen.getByText(
        "Debarquement de produits de la pêche maritime et de l'aquaculture marine sans notification prealable",
        { exact: false }
      )
    ).toBeInTheDocument()
    expect(screen.getByText('commentaire')).toBeInTheDocument()
  })

  it('renders fish infractions with natinfs', () => {
    render(
      <InfractionFishSummary
        infractions={[
          {
            natinf: 12345,
            threat: 'Dissimulation'
          },
          {
            natinf: 22182,
            threat: 'Entrave à la justice'
          }
        ]}
      />
    )
    expect(screen.getByText('NATINF : 12345', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('Infraction 1 : Dissimulation', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('NATINF : 22182', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('Infraction 2 : Entrave à la justice', { exact: false })).toBeInTheDocument()
  })
})
