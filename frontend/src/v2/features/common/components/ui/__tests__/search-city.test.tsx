import { vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { SearchCity } from '../search-city'
import * as useAddressService from '../../../services/use-address-service'
import { Formik } from 'formik'

vi.mock('../../../services/use-address-service', () => ({
  useAddressListQuery: vi.fn()
}))

describe('SearchCity', () => {
  const handleSubmit = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useAddressService.useAddressListQuery).mockReturnValue({
      data: undefined,
      isLoading: false,
      error: null
    } as any)
  })

  it('should render input with label', () => {
    render(
      <Formik initialValues={{ zipCode: '', city: '' }} onSubmit={handleSubmit}>
        <SearchCity name="zipCode" label="Lieu de contrôle" isLight={true} />
      </Formik>
    )
    expect(screen.getByText('Lieu de contrôle')).toBeInTheDocument()
  })

  it('should call useAddressListQuery with empty string initially', () => {
    render(
      <Formik initialValues={{ zipCode: '', city: '' }} onSubmit={handleSubmit}>
        <SearchCity name="zipCode" label="Lieu de contrôle" isLight={true} />
      </Formik>
    )
    expect(useAddressService.useAddressListQuery).toHaveBeenCalledWith('')
  })

  it('should call useAddressListQuery with initial city value from formik', () => {
    render(
      <Formik initialValues={{ zipCode: '75000', city: 'Paris' }} onSubmit={handleSubmit}>
        <SearchCity name="zipCode" label="Lieu de contrôle" isLight={true} />
      </Formik>
    )
    expect(useAddressService.useAddressListQuery).toHaveBeenCalledWith('Paris')
  })

  it('should render when city and zipCode are prepopulated', () => {
    vi.mocked(useAddressService.useAddressListQuery).mockReturnValue({
      data: undefined,
      isLoading: false,
      error: null
    } as any)

    const { container } = render(
      <Formik initialValues={{ zipCode: '75000', city: 'Paris' }} onSubmit={handleSubmit}>
        <SearchCity name="zipCode" label="Lieu de contrôle" isLight={true} />
      </Formik>
    )
    expect(container).toBeTruthy()
  })
})
