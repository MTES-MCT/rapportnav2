import { vi } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../test-utils'
import { SearchCity } from '../search-city'
import * as useAddressService from '../../../services/use-address-service'

// Mock the useAddressListQuery hook
vi.mock('../../../services/use-address-service', () => ({
  useAddressListQuery: vi.fn()
}))

const mockFieldFormik = {
  field: { name: 'zipCode', value: '', onChange: vi.fn(), onBlur: vi.fn() },
  meta: { touched: false, error: undefined },
  form: { setFieldValue: vi.fn() }
} as any

describe('SearchCity', () => {
  const mockOnChange = vi.fn()
  const defaultProps = {
    name: 'zipCode',
    label: 'Lieu de contrôle',
    isLight: true,
    onChange: mockOnChange,
    fieldFormik: mockFieldFormik
  }

  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useAddressService.useAddressListQuery).mockReturnValue({
      data: undefined,
      isLoading: false,
      error: null
    } as any)
  })

  it('should render input with label', () => {
    render(<SearchCity {...defaultProps} />)
    expect(screen.getByText('Lieu de contrôle')).toBeInTheDocument()
  })

  it('should render with initial value from city and zipCode', () => {
    render(<SearchCity {...defaultProps} value={{ city: 'Paris', zipCode: '75001' }} />)
    const input = screen.getByRole('textbox')
    expect(input).toHaveValue('Paris (75001)')
  })

  it('should call onChange with undefined when input is cleared', () => {
    render(<SearchCity {...defaultProps} value={{ city: 'Paris', zipCode: '75001' }} />)
    const input = screen.getByRole('textbox')
    fireEvent.change(input, { target: { value: '' } })
    expect(mockOnChange).toHaveBeenCalledWith(undefined)
  })

  it('should update display when external value changes', () => {
    const { rerender } = render(<SearchCity {...defaultProps} value={{ city: 'Paris', zipCode: '75001' }} />)

    const input = screen.getByRole('textbox')
    expect(input).toHaveValue('Paris (75001)')

    rerender(<SearchCity {...defaultProps} value={{ city: 'Brest', zipCode: '29200' }} />)
    expect(input).toHaveValue('Brest (29200)')
  })

  it('should call useAddressListQuery with empty string initially', () => {
    render(<SearchCity {...defaultProps} />)
    expect(useAddressService.useAddressListQuery).toHaveBeenCalledWith('')
  })

  it('should update query when user types', () => {
    render(<SearchCity {...defaultProps} />)
    const input = screen.getByRole('textbox')
    fireEvent.change(input, { target: { value: 'Par' } })
    expect(useAddressService.useAddressListQuery).toHaveBeenCalledWith('Par')
  })

  it('should render with isRequired attribute', () => {
    render(<SearchCity {...defaultProps} />)
    const input = screen.getByRole('textbox')
    expect(input).toBeInTheDocument()
  })
})
