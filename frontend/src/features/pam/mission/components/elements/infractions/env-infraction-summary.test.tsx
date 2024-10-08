import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import EnvInfractionSummary from './env-infraction-summary.tsx'
import { ControlType } from '@common/types/control-types.ts'
import { ActionTargetTypeEnum, InfractionTypeEnum, VesselTypeEnum } from '@common/types/env-mission-types.ts'
import { Infraction } from '@common/types/infraction-types.ts'
import { Icon } from '@mtes-mct/monitor-ui'

const infractionMock = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}

const infractionControlTypeNullMockEnv = {
  id: '123',
  controlType: null,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}

const infractionMockEnv = {
  id: '456',
  controlType: null,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: undefined
}
const infractionByTargetMock = (infractions: Infraction[]) => ({
  vesselIdentifier: '123',
  vesselType: VesselTypeEnum.COMMERCIAL,
  infractions,
  controlTypesWithInfraction: [ControlType.ADMINISTRATIVE],
  targetAddedByUnit: false
})

const infractionByTargetCompanyMock = (infractions: Infraction[]) => ({
  vesselIdentifier: null,
  vesselType: null,
  infractions,
  controlTypesWithInfraction: [ControlType.ADMINISTRATIVE],
  targetAddedByUnit: false
})

const props = infractionByTarget => ({
  infractionByTarget,
  onAddInfractionForTarget: vi.fn(),
  onEditInfractionForTarget: vi.fn(),
  onDeleteInfraction: vi.fn(),
  actionTargetType: ActionTargetTypeEnum.VEHICLE
})

const propsCompany = infractionByTarget => ({
  infractionByTarget,
  onAddInfractionForTarget: vi.fn(),
  onEditInfractionForTarget: vi.fn(),
  onDeleteInfraction: vi.fn(),
  actionTargetType: ActionTargetTypeEnum.COMPANY
})

describe('EnvInfractionSummary', () => {
  describe('testing rendering', () => {
    test('renders the component', async () => {
      render(<EnvInfractionSummary {...props(infractionByTargetMock([infractionMock]))} />)
      expect(screen.getByText('infraction pour cette cible')).toBeInTheDocument()
    })
    describe('Env infraction', () => {
      test('should render a specific control title', async () => {
        render(<EnvInfractionSummary {...props(infractionByTargetMock([infractionMockEnv]))} />)
        expect(screen.getByText('Infraction contrôle de l’environnement')).toBeInTheDocument()
      })
      test('should not render the edit and delete buttons when controlType is null', async () => {
        render(<EnvInfractionSummary {...props(infractionByTargetMock([infractionMockEnv]))} />)
        expect(screen.queryByRole('edit-infraction')).toBeNull()
        expect(screen.queryByRole('delete-infraction')).toBeNull()
      })
      test('should  render the display button when controlType is null', async () => {
        render(<EnvInfractionSummary {...props(infractionByTargetMock([infractionMockEnv]))} />)
        expect(screen.queryByRole('display-infraction')).not.toBeNull()
      })
    })
    describe('Nav infraction', () => {
      test('should render the correct control title', async () => {
        render(<EnvInfractionSummary {...props(infractionByTargetMock([infractionMock]))} />)
        expect(screen.getByTestId('env-infraction-control-title')).toHaveTextContent('Contrôle administratif navire')
      })
      test('should render the edit and delete buttons ', async () => {
        render(<EnvInfractionSummary {...props(infractionByTargetMock([infractionMock]))} />)
        expect(screen.getByRole('edit-infraction')).toBeInTheDocument()
        expect(screen.getByRole('delete-infraction')).toBeInTheDocument()
      })
    })
  })

  describe('clicking on buttons', () => {
    test('calls onAddInfractionForTarget when clicking the add button', async () => {
      const onAddInfractionForTarget = vi.fn()
      render(
        <EnvInfractionSummary {...{ ...props(infractionByTargetMock([infractionMock])), onAddInfractionForTarget }} />
      )
      const button = screen.getByRole('add-infraction')
      fireEvent.click(button)
      expect(onAddInfractionForTarget).toHaveBeenCalled()
    })
    test('calls onEditInfractionForTarget when clicking the edit button', async () => {
      const onEditInfractionForTarget = vi.fn()
      render(
        <EnvInfractionSummary {...{ ...props(infractionByTargetMock([infractionMock])), onEditInfractionForTarget }} />
      )
      const button = screen.getByRole('edit-infraction')
      fireEvent.click(button)
      expect(onEditInfractionForTarget).toHaveBeenCalled()
    })
    test('calls onDeleteInfraction when clicking the delete button', async () => {
      const onDeleteInfraction = vi.fn()
      render(<EnvInfractionSummary {...{ ...props(infractionByTargetMock([infractionMock])), onDeleteInfraction }} />)
      const button = screen.getByRole('delete-infraction')
      fireEvent.click(button)
      expect(onDeleteInfraction).toHaveBeenCalled()
    })
  })

  describe('rendering button "Ajouter une infraction pour cette cible"',  () => {
    test('should not render button "Ajouter une infraction pour cette cible" if action target type is not VEHICLE', async () => {
      render(<EnvInfractionSummary {...propsCompany(infractionByTargetCompanyMock([infractionMock]))} />)
      expect(screen.queryByText('infraction pour cette cible')).not.toBeInTheDocument()
    })
  })
})
