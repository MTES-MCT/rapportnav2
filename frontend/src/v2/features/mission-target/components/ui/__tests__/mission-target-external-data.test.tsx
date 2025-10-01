import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils.tsx'
import MissionTargetExternalData from '../mission-target-external-data'
import { TargetExternalData } from 'src/v2/features/common/types/target-types.ts'
import { FormalNoticeEnum, InfractionTypeEnum, VesselSizeEnum } from '@common/types/env-mission-types.ts'
import { VesselTypeEnum } from '@common/types/mission-types.ts'

// Mocks
vi.mock('../../../../common/hooks/use-vessel', () => ({
  useVessel: () => ({
    getVesselTypeName: vi.fn().mockReturnValue(VesselTypeEnum.COMMERCIAL),
    getVesselSize: vi.fn().mockReturnValue(VesselSizeEnum.FROM_12_TO_24m)
  })
}))

describe('MissionTargetExternalData', () => {
  let baseData: TargetExternalData

  beforeEach(() => {
    baseData = {
      id: '1',
      registrationNumber: 'AB-123',
      vesselType: VesselTypeEnum.COMMERCIAL,
      vesselSize: VesselSizeEnum.FROM_12_TO_24m,
      controlledPersonIdentity: 'Jean Dupont',
      infractionType: InfractionTypeEnum.WITH_REPORT,
      formalNotice: FormalNoticeEnum.YES,
      natinfs: ['1234', '5678'],
      relevantCourt: 'Tribunal de Paris',
      toProcess: true,
      observations: 'Some notes'
    }
  })

  it('renders summary when showDetail=false', () => {
    render(<MissionTargetExternalData externalData={baseData} showDetail={false} />)
    expect(screen.getByText('Infraction contrôle de l’environnement')).toBeInTheDocument()
    expect(screen.getByText('Avec PV')).toBeInTheDocument()
    expect(screen.getByText('2 NATINF : 1234, 5678')).toBeInTheDocument()
  })

  it('renders details when showDetail=true', () => {
    render(<MissionTargetExternalData externalData={baseData} showDetail={true} />)

    expect(screen.getByText('Immatriculation')).toBeInTheDocument()
    expect(screen.getByText('AB-123')).toBeInTheDocument()
    expect(screen.getByText('COMMERCIAL')).toBeInTheDocument()
    expect(screen.getByText('FROM_12_TO_24m')).toBeInTheDocument()
    expect(screen.getByText('Jean Dupont')).toBeInTheDocument()
    expect(screen.getByText('Tribunal de Paris')).toBeInTheDocument()
    expect(screen.getByLabelText('À traiter')).toBeChecked()
    expect(screen.getByText('Some notes')).toBeInTheDocument()
  })

  it('renders "--" when fields are missing', () => {
    const minimalData: TargetExternalData = { id: '2' }
    render(<MissionTargetExternalData externalData={minimalData} showDetail={true} />)

    expect(screen.getByTestId('observations')).toHaveTextContent('--')
    expect(screen.getByLabelText('À traiter')).not.toBeChecked()
  })
})
