import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import EnvInfractionTargetAddedByUnitForm from './env-infraction-target-added-by-unit-form.tsx'
import { ControlType } from '@common/types/control-types.ts'
import {
  ActionTargetTypeEnum,
  FormalNoticeEnum,
  InfractionTypeEnum, VehicleTypeEnum,
  VesselSizeEnum,
  VesselTypeEnum
} from '@common/types/env-mission-types.ts'
import { Natinf } from '@common/types/infraction-types.ts'

const targetVesselMock = {
  id: '123',
  vehicleType: VehicleTypeEnum.VESSEL
}

const targetMock = {
  id: '123',
  vehicleType: null
}

const infractionVesselMock = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: targetVesselMock
}

const infractionMock = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: targetMock
}

const propsVehicleVessel = (infraction = infractionVesselMock) => ({
  actionTargetType: ActionTargetTypeEnum.VEHICLE,
  infraction,
  availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
  onChange: vi.fn(),
  onChangeTarget: vi.fn(),
  onCancel: vi.fn()
})

const propsVehicleWithoutVessel = (infraction = infractionMock) => ({
  actionTargetType: ActionTargetTypeEnum.VEHICLE,
  infraction,
  availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
  onChange: vi.fn(),
  onChangeTarget: vi.fn(),
  onCancel: vi.fn()
})

const propsCompany = (infraction = infractionMock) => ({
  actionTargetType: ActionTargetTypeEnum.COMPANY,
  infraction,
  availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
  onChange: vi.fn(),
  onChangeTarget: vi.fn(),
  onCancel: vi.fn()
})

const propsIndividual = (infraction = infractionMock) => ({
  actionTargetType: ActionTargetTypeEnum.INDIVIDUAL,
  infraction,
  availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
  onChange: vi.fn(),
  onChangeTarget: vi.fn(),
  onCancel: vi.fn()
})

describe('EnvInfractionTargetAddedByUnitForm', () => {
  test('renders the component', async () => {
    render(<EnvInfractionTargetAddedByUnitForm {...propsVehicleVessel()} />)
    expect(screen.getByText('Type de contrôle avec infraction')).toBeInTheDocument()
  })
  describe('changing the target data', () => {
    test('calls onChangeTarget when changing vesselIdentifier', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsVehicleVessel(), onChangeTarget }} />)
      const field = screen.getByRole('vesselIdentifier')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(onChangeTarget).toHaveBeenCalled()
    })
    test('calls onChangeTarget when changing identityControlledPerson', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsVehicleVessel(), onChangeTarget }} />)
      const field = screen.getByRole('identityControlledPerson')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(onChangeTarget).toHaveBeenCalled()
    })

    test('should display vessel inputs when VehicleType is VESSEL', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsVehicleVessel(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('stack-vessel-infraction-env')

      expect(inputsVessel).toBeInTheDocument()
    })

    test('should not display vessel inputs when VehicleType is not VESSEL', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsVehicleWithoutVessel(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('stack-vessel-infraction-env')

      expect(inputsVessel).toBeNull()
    })

    test('should display vesselIdentifier input when ActionTargetType is VEHICLE', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsVehicleWithoutVessel(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('vessel-identifier')

      expect(inputsVessel).toBeInTheDocument()
    })

    test('should not display vessel inputs when ActionTargetType is COMPANY', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsCompany(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('stack-vessel-infraction-env')

      expect(inputsVessel).toBeNull()
    })

    test('should not display vesselIdentifier input when ActionTargetType is COMPANY', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsCompany(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('vessel-identifier')

      expect(inputsVessel).toBeNull()
    })

    test('should not display vessel inputs when ActionTargetType is INDIVIDUAL', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsIndividual(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('stack-vessel-infraction-env')

      expect(inputsVessel).toBeNull()
    })

    test('should not display vesselIdentifier input when ActionTargetType is INDIVIDUAL', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsIndividual(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('vessel-identifier')

      expect(inputsVessel).toBeNull()
    })

    test('should display label "Identité de la personne contrôlée" input when ActionTargetType is INDIVIDUAL', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsIndividual(), onChangeTarget }} />)

      const label = screen.getByLabelText(/Identité de la personne contrôlée/i)

      expect(label).toBeInTheDocument()
    })

    test('should display label "Identité de la personne morale contrôlée" when ActionTargetType is COMPANY', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsCompany(), onChangeTarget }} />)

      const label = screen.getByLabelText(/Identité de la personne morale contrôlée/i)

      expect(label).toBeInTheDocument()
    })


  })
})
