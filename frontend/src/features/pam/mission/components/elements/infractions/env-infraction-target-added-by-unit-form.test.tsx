import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import EnvInfractionTargetAddedByUnitForm from './env-infraction-target-added-by-unit-form.tsx'
import { ControlType } from '@common/types/control-types.ts'
import {
  FormalNoticeEnum,
  InfractionTypeEnum, VehicleTypeEnum,
  VesselSizeEnum,
  VesselTypeEnum
} from '@common/types/env-mission-types.ts'
import { Natinf } from '@common/types/infraction-types.ts'

const infractionTargetMock = {
  id: '123',
  vehicleType: VehicleTypeEnum.VESSEL
}

const infractionPersonTargetMock = {
  id: '123',
  vehicleType: null
}

const infractionMock = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: infractionTargetMock
}

const infractionPersonMock = {
  id: '123',
  controlType: ControlType.ADMINISTRATIVE,
  infractionType: InfractionTypeEnum.WITHOUT_REPORT,
  natinfs: ['123'],
  observations: undefined,
  target: infractionPersonTargetMock
}

const props = (infraction = infractionMock) => ({
  infraction,
  availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
  onChange: vi.fn(),
  onChangeTarget: vi.fn(),
  onCancel: vi.fn()
})

const propsPerson = (infraction = infractionPersonMock) => ({
  infraction,
  availableControlTypesForInfraction: [ControlType.ADMINISTRATIVE],
  onChange: vi.fn(),
  onChangeTarget: vi.fn(),
  onCancel: vi.fn()
})
describe('EnvInfractionTargetAddedByUnitForm', () => {
  test('renders the component', async () => {
    render(<EnvInfractionTargetAddedByUnitForm {...props()} />)
    expect(screen.getByText('Type de contrôle avec infraction')).toBeInTheDocument()
  })
  describe('changing the target data', () => {
    test('calls onChangeTarget when changing vesselIdentifier', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...props(), onChangeTarget }} />)
      const field = screen.getByRole('vesselIdentifier')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(onChangeTarget).toHaveBeenCalled()
    })
    test('calls onChangeTarget when changing identityControlledPerson', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...props(), onChangeTarget }} />)
      const field = screen.getByRole('identityControlledPerson')
      const value = 'test'
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(onChangeTarget).toHaveBeenCalled()
    })

    test('should not display vessel or vehicle inputs when VehicleType is null', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...propsPerson(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('stack-vessel-infraction-env')

      expect(inputsVessel).toBeNull()
    })

    test('should display vessel or vehicle inputs when VehicleType is not null', async () => {
      const onChangeTarget = vi.fn()
      render(<EnvInfractionTargetAddedByUnitForm {...{ ...props(), onChangeTarget }} />)

      const inputsVessel = screen.queryByTestId('stack-vessel-infraction-env')

      expect(inputsVessel).toBeInTheDocument()
    })
  })
})
