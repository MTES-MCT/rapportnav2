import { render, screen } from '../../../../../../test-utils.tsx'
import MissionInfractionFishSummary from '../mission-infraction-fish-summary.tsx'

describe('MissionInfractionFishSummary', () => {
  it('should display infraction, category, description and comment', () => {
    render(
      <MissionInfractionFishSummary
        infractions={[
          {
            natinf: 12345,
            threat: 'Dissimulation',
            threatCharacterization:
              "Debarquement de produits de la peche maritime et de l'aquaculture marine sans notification prealable",
            comments: 'commentaire'
          }
        ]}
      />
    )
    expect(screen.getByText('Infraction : Dissimulation')).toBeInTheDocument()
    expect(screen.getByText(/NATINF\s*:\s*12345/i)).toBeInTheDocument()
    expect(
      screen.getByText(
        "Debarquement de produits de la peche maritime et de l'aquaculture marine sans notification prealable",
        { exact: false }
      )
    ).toBeInTheDocument()
    expect(screen.getByText('commentaire')).toBeInTheDocument()
  })
})
