import { DiscardedSpeciesControl, DiscardReason, MissionActionType, SpeciesControl } from '@common/types/fish-mission-types'
import { fireEvent } from '@testing-library/react'
import { render, screen } from '../../../../../../test-utils'
import { MissionFishActionData } from '../../../../common/types/mission-action'
import { SatiModuleType } from '../../../../common/types/sati'
import FishControlSpeciesSection from '../fish-control-species-section'

const baseAction: MissionFishActionData = {
  faoAreas: [],
  fishActionType: MissionActionType.LAND_CONTROL,
  userTrigram: 'ABC',
  fishInfractions: []
}

const actionWithModule = (module: SatiModuleType, overrides: Partial<MissionFishActionData> = {}) =>
  ({
    ...baseAction,
    ...overrides,
    sati: { module, actionId: 'a1', principalInspector: { isOutOfUnit: false } }
  }) as MissionFishActionData

const buildSpecies = (overrides: Partial<SpeciesControl> = {}): SpeciesControl => ({
  speciesCode: 'COD',
  speciesName: 'Cabillaud',
  isNotLanded: false,
  nbFish: undefined,
  declaredWeight: 100,
  controlledWeight: 90,
  underSized: undefined,
  underSizedWeight: 5,
  presentationCodes: ['WHL'],
  faoZones: ['27.7.d'],
  ...overrides
})

const buildDiscarded = (overrides: Partial<DiscardedSpeciesControl> = {}): DiscardedSpeciesControl => ({
  speciesCode: 'HKE',
  rejectedWeight: 12,
  discardReason: DiscardReason.DIM,
  faoZones: ['27.7.d'],
  ...overrides
})

describe('FishControlSpeciesSection', () => {
  it('renders the section title', () => {
    render(<FishControlSpeciesSection action={actionWithModule(SatiModuleType.M1)} />)
    expect(screen.getByText('Inspection des espèces')).toBeInTheDocument()
  })

  it('shows section titles and the Pesé column for M3', () => {
    render(
      <FishControlSpeciesSection
        action={{ ...actionWithModule(SatiModuleType.M3), speciesOnboard: [buildSpecies()] }}
      />
    )
    expect(screen.getByText('Pour les espèces débarquées')).toBeInTheDocument()
    expect(screen.getByText('Pour les espèces non débarquées')).toBeInTheDocument()
    expect(screen.getByText('Pesé')).toBeInTheDocument()
  })

  it('hides section titles and shows the Estimé column for M1', () => {
    render(
      <FishControlSpeciesSection
        action={{ ...actionWithModule(SatiModuleType.M1), speciesOnboard: [buildSpecies()] }}
      />
    )
    expect(screen.queryByText('Pour les espèces débarquées')).toBeNull()
    expect(screen.queryByText('Pour les espèces non débarquées')).toBeNull()
    expect(screen.getByText('Estimé')).toBeInTheDocument()
  })

  it('shows rows specific to M3 only for M3', () => {
    render(<FishControlSpeciesSection action={actionWithModule(SatiModuleType.M3)} />)
    expect(screen.getByText('Cale contrôlée après déchargement')).toBeInTheDocument()
    expect(screen.queryByText('Arrimage séparé des espèces soumises à plan')).toBeNull()
  })

  it('shows rows specific to M1 only for M1', () => {
    render(<FishControlSpeciesSection action={actionWithModule(SatiModuleType.M1)} />)
    expect(screen.getByText('Arrimage séparé des espèces soumises à plan')).toBeInTheDocument()
    expect(screen.queryByText('Cale contrôlée après déchargement')).toBeNull()
  })

  it('renders species onboard rows', () => {
    render(
      <FishControlSpeciesSection
        action={{ ...actionWithModule(SatiModuleType.M1), speciesOnboard: [buildSpecies()] }}
      />
    )
    expect(screen.getByText('COD – Cabillaud')).toBeInTheDocument()
    expect(screen.getByText('100 kg')).toBeInTheDocument()
    expect(screen.getByText('90 kg')).toBeInTheDocument()
    expect(screen.getByText('WHL')).toBeInTheDocument()
    expect(screen.getByText('27.7.d')).toBeInTheDocument()
  })

  it('does not render the species table when there is no species onboard', () => {
    render(<FishControlSpeciesSection action={actionWithModule(SatiModuleType.M1)} />)
    expect(screen.queryByText('Espèce(s)')).toBeNull()
  })

  it('shows the Marge column and edit action only for M3', () => {
    render(
      <FishControlSpeciesSection
        action={{ ...actionWithModule(SatiModuleType.M3), speciesOnboard: [buildSpecies()] }}
      />
    )
    expect(screen.getByText('Marge')).toBeInTheDocument()
    expect(screen.getByRole('button')).toBeInTheDocument()
  })

  it('hides the Marge column and edit action for M1', () => {
    render(
      <FishControlSpeciesSection
        action={{ ...actionWithModule(SatiModuleType.M1), speciesOnboard: [buildSpecies()] }}
      />
    )
    expect(screen.queryByText('Marge')).toBeNull()
    expect(screen.queryByRole('button')).toBeNull()
  })

  it('opens the marge dialog for the clicked species when M3', () => {
    render(
      <FishControlSpeciesSection
        action={{
          ...actionWithModule(SatiModuleType.M3),
          speciesOnboard: [buildSpecies({ speciesCode: 'COD', speciesName: 'Cabillaud' })]
        }}
      />
    )
    fireEvent.click(screen.getByRole('button'))
    expect(screen.getByText('Mise à jour marge (COD – Cabillaud)')).toBeInTheDocument()
  })

  it('renders discarded species for non-M3 modules', () => {
    render(
      <FishControlSpeciesSection
        action={{ ...actionWithModule(SatiModuleType.M1), discardedSpecies: [buildDiscarded()] }}
      />
    )
    expect(screen.getByText('Rejets')).toBeInTheDocument()
    expect(screen.getByText('HKE')).toBeInTheDocument()
    expect(screen.getByText('12 kg')).toBeInTheDocument()
  })

  it('does not render discarded species for M3', () => {
    render(
      <FishControlSpeciesSection
        action={{ ...actionWithModule(SatiModuleType.M3), discardedSpecies: [buildDiscarded()] }}
      />
    )
    expect(screen.queryByText('Rejets')).toBeNull()
  })

  it('does not render observations when there are none', () => {
    render(<FishControlSpeciesSection action={actionWithModule(SatiModuleType.M1)} />)
    expect(screen.queryByText(/Observations \(hors infractions\)/)).toBeNull()
  })

  it('renders species observations when present', () => {
    render(
      <FishControlSpeciesSection
        action={actionWithModule(SatiModuleType.M1, { speciesObservations: 'Taille non conforme' })}
      />
    )
    expect(screen.getByText('Observations (hors infractions) sur les espèces')).toBeInTheDocument()
    expect(screen.getByText('Taille non conforme')).toBeInTheDocument()
  })
})
