import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionActionItemSectorControlForm from '../mission-action-control-sector-form'
import { SectorType, SectorFishingType, SectorPleasureType } from '../../../../common/types/sector-types'
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

describe('MissionActionItemSectorControlForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should show search port when FISHING and LANDING_SITE', () => {
    const formik = createMockFormik({
      sectorType: SectorType.FISHING,
      sectorEstablishmentType: SectorFishingType.LANDING_SITE
    })
    render(<MissionActionItemSectorControlForm formik={formik} />)

    expect(screen.getByTestId('search-port')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
    expect(screen.queryByTestId('search-establishment')).not.toBeInTheDocument()
  })

  it('should show fish auction select when FISHING and FISH_AUCTION', () => {
    const formik = createMockFormik({
      sectorType: SectorType.FISHING,
      sectorEstablishmentType: SectorFishingType.FISH_AUCTION
    })
    render(<MissionActionItemSectorControlForm formik={formik} />)

    expect(screen.getByTestId('select-fish-auction')).toBeInTheDocument()
    expect(screen.queryByTestId('search-establishment')).not.toBeInTheDocument()
    expect(screen.queryByTestId('search-port')).not.toBeInTheDocument()
  })

  it('should show FormikEstablishment when FISHING and GMS', () => {
    const formik = createMockFormik({
      sectorType: SectorType.FISHING,
      sectorEstablishmentType: SectorFishingType.GMS
    })
    render(<MissionActionItemSectorControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
    expect(screen.queryByTestId('search-port')).not.toBeInTheDocument()
  })

  it('should show FormikEstablishment when PLEASURE and PLEASURE_MARKET', () => {
    const formik = createMockFormik({
      sectorType: SectorType.PLEASURE,
      sectorEstablishmentType: SectorPleasureType.PLEASURE_MARKET
    })
    render(<MissionActionItemSectorControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
  })

  it('should show FormikEstablishment when FISHING and RESTAURANT', () => {
    const formik = createMockFormik({
      sectorType: SectorType.FISHING,
      sectorEstablishmentType: SectorFishingType.RESTAURANT
    })
    render(<MissionActionItemSectorControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
  })

  it('should show FormikEstablishment when no sector type is selected', () => {
    const formik = createMockFormik({
      sectorType: undefined,
      sectorEstablishmentType: undefined
    })
    render(<MissionActionItemSectorControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
  })

  it('should display Lieu de controle label for SearchAddress', () => {
    const formik = createMockFormik({
      sectorType: SectorType.FISHING,
      sectorEstablishmentType: SectorFishingType.LANDING_SITE
    })
    render(<MissionActionItemSectorControlForm formik={formik} />)

    expect(screen.getByText('Lieu de contrôle')).toBeInTheDocument()
  })
})
