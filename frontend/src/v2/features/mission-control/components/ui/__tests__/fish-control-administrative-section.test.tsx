import { ControlCheck, MissionActionType } from '@common/types/fish-mission-types'
import { render, screen } from '../../../../../../test-utils'
import { MissionFishActionData } from '../../../../common/types/mission-action'
import { SatiModuleType } from '../../../../common/types/sati'
import FishControlAdministrativeSection from '../fish-control-administrative-section'

const baseAction: MissionFishActionData = {
  faoAreas: [],
  fishActionType: MissionActionType.SEA_CONTROL,
  userTrigram: 'ABC',
  fishInfractions: []
}

const actionWithModule = (module: SatiModuleType, overrides: Partial<MissionFishActionData> = {}) =>
  ({
    ...baseAction,
    ...overrides,
    sati: { module, actionId: 'a1', principalInspector: { isOutOfUnit: false } }
  }) as MissionFishActionData

describe('FishControlAdministrativeSection', () => {
  it('renders the section title and column headers', () => {
    render(<FishControlAdministrativeSection action={actionWithModule(SatiModuleType.M1)} />)
    expect(screen.getByText('Conformité du navire')).toBeInTheDocument()
    expect(screen.getByText('Oui')).toBeInTheDocument()
    expect(screen.getByText('Non')).toBeInTheDocument()
    expect(screen.getByText('N/A')).toBeInTheDocument()
  })

  it('renders rows that are always visible regardless of module', () => {
    render(<FishControlAdministrativeSection action={actionWithModule(SatiModuleType.M1)} />)
    expect(screen.getByText('Bonne émission VMS')).toBeInTheDocument()
    expect(screen.getByText('Bonne émission AIS')).toBeInTheDocument()
  })

  it('shows the gangway row and hides the port entrance row for M1', () => {
    render(<FishControlAdministrativeSection action={actionWithModule(SatiModuleType.M1)} />)
    expect(screen.getByText('Echelle de coupée présente et conforme')).toBeInTheDocument()
    expect(screen.queryByText('Accès au port / autorisation de débarquement conformes')).toBeNull()
  })

  it('shows the port entrance row and hides the gangway row for M3', () => {
    render(<FishControlAdministrativeSection action={actionWithModule(SatiModuleType.M3)} />)
    expect(screen.getByText('Accès au port / autorisation de débarquement conformes')).toBeInTheDocument()
    expect(screen.queryByText('Echelle de coupée présente et conforme')).toBeNull()
  })

  it('hides the weighing certificate row when the weighing permit is not YES', () => {
    render(
      <FishControlAdministrativeSection
        action={actionWithModule(SatiModuleType.M1, { onboardWeighingPermit: ControlCheck.NO })}
      />
    )
    expect(screen.queryByText('Certificat de pesée présent et systèmes de pesée à bord valides')).toBeNull()
  })

  it('shows the weighing certificate row when the weighing permit is YES', () => {
    render(
      <FishControlAdministrativeSection
        action={actionWithModule(SatiModuleType.M1, { onboardWeighingPermit: ControlCheck.YES })}
      />
    )
    expect(screen.getByText('Certificat de pesée présent et systèmes de pesée à bord valides')).toBeInTheDocument()
  })

  it('does not render observations when there are none', () => {
    render(<FishControlAdministrativeSection action={actionWithModule(SatiModuleType.M1)} />)
    expect(screen.queryByText(/Observations \(hors infractions\)/)).toBeNull()
  })

  it('renders observations when present', () => {
    render(
      <FishControlAdministrativeSection
        action={actionWithModule(SatiModuleType.M1, { licencesAndLogbookObservations: 'Licence manquante' })}
      />
    )
    expect(screen.getByText('Observations (hors infractions) sur les obligations déclaratives / autorisations')).toBeInTheDocument()
    expect(screen.getByText('Licence manquante')).toBeInTheDocument()
  })
})
