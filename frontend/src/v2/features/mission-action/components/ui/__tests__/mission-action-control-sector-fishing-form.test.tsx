import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionActionItemSectorFishingControlForm from '../mission-action-control-sector-fishing-form'
import { SectorFishingType } from '../../../../common/types/sector-types'
import { FormikProps } from 'formik'
import { ActionControlInput } from '../../../types/action-type'

vi.mock('../../../../common/services/use-fish-auction-service', () => ({
  useFishAuctionListQuery: vi.fn().mockReturnValue({ data: [] })
}))

const createMockFormik = (values: Partial<ActionControlInput>): FormikProps<ActionControlInput> =>
  ({
    values: {
      dates: [undefined, undefined],
      geoCoords: [undefined, undefined],
      ...values
    },
    errors: {},
    initialValues: {} as ActionControlInput
  }) as FormikProps<ActionControlInput>

describe('MissionActionItemSectorFishingControlForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should show search port when LANDING_SITE', () => {
    const formik = createMockFormik({ sectorEstablishmentType: SectorFishingType.LANDING_SITE })
    render(<MissionActionItemSectorFishingControlForm formik={formik} />)

    expect(screen.getByTestId('search-port')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
    expect(screen.queryByTestId('search-establishment')).not.toBeInTheDocument()
  })

  it('should show fish auction select when FISH_AUCTION', () => {
    const formik = createMockFormik({ sectorEstablishmentType: SectorFishingType.FISH_AUCTION })
    render(<MissionActionItemSectorFishingControlForm formik={formik} />)

    expect(screen.getByTestId('select-fish-auction')).toBeInTheDocument()
    expect(screen.queryByTestId('search-establishment')).not.toBeInTheDocument()
    expect(screen.queryByTestId('search-port')).not.toBeInTheDocument()
  })

  it('should show FormikEstablishment when GMS', () => {
    const formik = createMockFormik({ sectorEstablishmentType: SectorFishingType.GMS })
    render(<MissionActionItemSectorFishingControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
    expect(screen.queryByTestId('search-port')).not.toBeInTheDocument()
  })

  it('should show FormikEstablishment when RESTAURANT', () => {
    const formik = createMockFormik({ sectorEstablishmentType: SectorFishingType.RESTAURANT })
    render(<MissionActionItemSectorFishingControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
  })

  it('should show FormikEstablishment when no establishment type is selected', () => {
    const formik = createMockFormik({ sectorEstablishmentType: undefined })
    render(<MissionActionItemSectorFishingControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
  })

  it('should display Lieu de controle label for the port search', () => {
    const formik = createMockFormik({ sectorEstablishmentType: SectorFishingType.LANDING_SITE })
    render(<MissionActionItemSectorFishingControlForm formik={formik} />)

    expect(screen.getByText('Lieu de contrôle')).toBeInTheDocument()
  })
})
