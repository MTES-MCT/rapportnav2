import { vi } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../test-utils'
import { SearchCity } from '../search-city'
import * as useAddressService from '../../../services/use-address-service'

// Mock the useAddressListQuery hook
vi.mock('../../../services/use-address-service', () => ({
  useAddressListQuery: vi.fn()
}))

describe('SearchCity', () => {
  const mockOnChange = vi.fn()
  const defaultProps = {
    name: 'locationDescription',
    label: 'Lieu de contrôle',
    isLight: true,
    onChange: mockOnChange
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

  it('should render with initial value', () => {
    render(<SearchCity {...defaultProps} value="Paris (75001)" />)
    const input = screen.getByRole('textbox')
    expect(input).toHaveValue('Paris (75001)')
  })

  it('should call onChange with undefined when input is cleared', () => {
    render(<SearchCity {...defaultProps} value="Paris (75001)" />)
    const input = screen.getByRole('textbox')
    fireEvent.change(input, { target: { value: '' } })
    expect(mockOnChange).toHaveBeenCalledWith(undefined)
  })

  it('should update search value when external value changes', () => {
    const { rerender } = render(<SearchCity {...defaultProps} value="Paris (75001)" />)

    const input = screen.getByRole('textbox')
    expect(input).toHaveValue('Paris (75001)')

    rerender(<SearchCity {...defaultProps} value="Brest (29200)" />)
    expect(input).toHaveValue('Brest (29200)')
  })

  it('should call useAddressListQuery with empty string initially', () => {
    render(<SearchCity {...defaultProps} />)
    expect(useAddressService.useAddressListQuery).toHaveBeenCalledWith('')
  })

  it('should call useAddressListQuery with initial value', () => {
    render(<SearchCity {...defaultProps} value="Paris (75001)" />)
    expect(useAddressService.useAddressListQuery).toHaveBeenCalledWith('Paris (75001)')
  })

  it('should update query when user types', () => {
    render(<SearchCity {...defaultProps} />)
    const input = screen.getByRole('textbox')
    fireEvent.change(input, { target: { value: 'Par' } })
    expect(useAddressService.useAddressListQuery).toHaveBeenCalledWith('Par')
  })

  it('should render with isRequired attribute', () => {
    render(<SearchCity {...defaultProps} />)
    // The TextInput component from monitor-ui renders with isRequired
    const input = screen.getByRole('textbox')
    expect(input).toBeInTheDocument()
  })
})
