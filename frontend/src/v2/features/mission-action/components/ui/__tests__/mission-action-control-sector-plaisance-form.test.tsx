import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionActionItemSectorPlaisanceControlForm from '../mission-action-control-sector-plaisance-form'
import { SectorPleasureType } from '../../../../common/types/sector-types'
import { FormikProps } from 'formik'
import { ActionControlInput } from '../../../types/action-type'

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

describe('MissionActionItemSectorPlaisanceControlForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should show FormikEstablishment when PLEASURE_MARKET', () => {
    const formik = createMockFormik({ sectorEstablishmentType: SectorPleasureType.PLEASURE_MARKET })
    render(<MissionActionItemSectorPlaisanceControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
    expect(screen.queryByTestId('search-city')).not.toBeInTheDocument()
  })

  it('should show FormikEstablishment when no establishment type is selected', () => {
    const formik = createMockFormik({ sectorEstablishmentType: undefined })
    render(<MissionActionItemSectorPlaisanceControlForm formik={formik} />)

    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
  })

  it('should never show the port search nor the fish auction select', () => {
    const formik = createMockFormik({ sectorEstablishmentType: SectorPleasureType.SEA_DRIVING_LESSON })
    render(<MissionActionItemSectorPlaisanceControlForm formik={formik} />)

    expect(screen.queryByTestId('search-port')).not.toBeInTheDocument()
    expect(screen.queryByTestId('select-fish-auction')).not.toBeInTheDocument()
  })
})
