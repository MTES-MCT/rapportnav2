import { VehicleTypeEnum } from '@common/types/env-mission-types'
import { VesselTypeEnum } from '@common/types/mission-types'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionTargetTitle from '../mission-target-title'

// ---- Mock hooks ----
const mockGetVesselTypeName = vi.fn()
const mockGetVehiculeType = vi.fn()

vi.mock('../../../../common/hooks/use-vessel', () => ({
  useVessel: () => ({
    getVesselTypeName: mockGetVesselTypeName
  })
}))

vi.mock('../../../../common/hooks/use-vehicule', () => ({
  useVehicule: () => ({
    getVehiculeType: mockGetVehiculeType
  })
}))

describe('MissionTargetTitle', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders vehicle only when only vehicleType is provided', () => {
    mockGetVehiculeType.mockReturnValue('Car')
    mockGetVesselTypeName.mockReturnValue('')

    render(<MissionTargetTitle vehicleType={VehicleTypeEnum.VEHICLE_LAND} />)

    expect(screen.getByTestId('target-title').textContent).toBe('Car')
  })

  it('renders vesselType only when only vessel data exists', () => {
    mockGetVehiculeType.mockReturnValue('')
    mockGetVesselTypeName.mockReturnValue('Fishing Vessel')

    render(
      <MissionTargetTitle
        target={{
          externalData: { vesselType: VesselTypeEnum.FISHING }
        }}
      />
    )

    expect(screen.getByTestId('target-title').textContent).toBe('Fishing Vessel')
  })

  it('renders id only when only id data exists', () => {
    mockGetVehiculeType.mockReturnValue('')
    mockGetVesselTypeName.mockReturnValue('')

    render(
      <MissionTargetTitle
        target={{
          identityControlledPerson: 'John Doe'
        }}
      />
    )

    expect(screen.getByTestId('target-title').textContent).toBe('John Doe')
  })

  it('renders vehicle - vesselType - id when all are present', () => {
    mockGetVehiculeType.mockReturnValue('Car')
    mockGetVesselTypeName.mockReturnValue('Fishing Vessel')

    render(
      <MissionTargetTitle
        vehicleType={VehicleTypeEnum.VEHICLE_LAND}
        target={{
          externalData: {
            registrationNumber: 'AB-123',
            vesselType: VesselTypeEnum.FISHING
          }
        }}
      />
    )

    // Expected format: "Car - Fishing Vessel - AB-123"
    expect(screen.getByTestId('target-title').textContent).toBe('Car - Fishing Vessel - AB-123')
  })

  it('renders correctly when vesselType exists but vehicle is empty', () => {
    mockGetVehiculeType.mockReturnValue('')
    mockGetVesselTypeName.mockReturnValue('Sailboat')

    render(
      <MissionTargetTitle
        target={{
          vesselType: 'SAIL'
        }}
      />
    )

    expect(screen.getByTestId('target-title').textContent).toBe('Sailboat')
  })

  it('renders empty string when no data is provided', () => {
    mockGetVehiculeType.mockReturnValue('')
    mockGetVesselTypeName.mockReturnValue('')

    render(<MissionTargetTitle />)

    expect(screen.getByTestId('target-title').textContent).toBe('')
  })

  it('renders tag for Vehicle', () => {
    const nbTarget = 2
    mockGetVehiculeType.mockReturnValue('')
    mockGetVesselTypeName.mockReturnValue('')

    render(
      <MissionTargetTitle
        target={{
          externalData: {
            id: '3',
            nbTarget,
            vesselType: VesselTypeEnum.SAILING_LEISURE
          }
        }}
      />
    )
    expect(screen.getByTestId('target-tag').textContent).toBe(nbTarget.toString())
    expect(screen.getByTestId('target-title').textContent).toBe('type de véhicule en infraction')
  })

  it('renders tag for Person', () => {
    const nbTarget = 2
    mockGetVehiculeType.mockReturnValue('')
    mockGetVesselTypeName.mockReturnValue('')

    render(
      <MissionTargetTitle
        target={{
          externalData: {
            id: '3',
            nbTarget
          }
        }}
      />
    )
    expect(screen.getByTestId('target-tag').textContent).toBe(nbTarget.toString())
    expect(screen.getByTestId('target-title').textContent).toBe('personnes physiques/morales en infraction')
  })
})
