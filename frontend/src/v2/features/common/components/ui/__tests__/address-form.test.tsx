import { fireEvent, render, screen, waitFor } from '../../../../../../test-utils'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { Address } from '../../../types/sati'
import AddressForm from '../address-form'

vi.mock('../../../../sati/components/ui/address-form-item', () => ({
  AddressFormItem: ({ readOnly, disabled }: { readOnly?: boolean; disabled?: boolean }) => (
    <div data-testid="address-form-item" data-readonly={String(!!readOnly)} data-disabled={String(!!disabled)} />
  )
}))

const completeAddress: Address = { street: '1 rue de la mer', town: 'Brest', zipcode: '29200', country: 'FRA' }

describe('AddressForm', () => {
  const onChange = vi.fn()

  beforeEach(() => vi.clearAllMocks())

  it('should render the panel title', () => {
    render(<AddressForm onChange={onChange} />)

    expect(screen.getByText('Adresse de l’établissement contrôlé')).toBeInTheDocument()
  })

  it('should start in edit mode when the address has no street', () => {
    render(<AddressForm onChange={onChange} address={{ town: 'Brest' }} />)

    expect(screen.getByTestId('address-form-item')).toHaveAttribute('data-readonly', 'false')
    expect(screen.getByTestId('validate-address')).toBeInTheDocument()
    expect(screen.getByText('Annuler')).toBeInTheDocument()
  })

  it('should start in edit mode without any address', () => {
    render(<AddressForm onChange={onChange} />)

    expect(screen.getByTestId('address-form-item')).toHaveAttribute('data-readonly', 'false')
  })

  it('should start read only when the address has a street', () => {
    render(<AddressForm onChange={onChange} address={completeAddress} />)

    expect(screen.getByTestId('address-form-item')).toHaveAttribute('data-readonly', 'true')
    expect(screen.queryByTestId('validate-address')).not.toBeInTheDocument()
    expect(screen.getByTitle('Editer')).toBeInTheDocument()
  })

  it('should pass disabled to the address fields', () => {
    render(<AddressForm onChange={onChange} disabled />)

    expect(screen.getByTestId('address-form-item')).toHaveAttribute('data-disabled', 'true')
  })

  it('should disable validation while the address is incomplete', async () => {
    render(<AddressForm onChange={onChange} address={{ town: 'Brest' }} />)

    await waitFor(() => expect(screen.getByTestId('validate-address')).toBeDisabled())
  })

  it('should send the address and leave edit mode when validating a complete address', async () => {
    render(<AddressForm onChange={onChange} address={completeAddress} />)
    fireEvent.click(screen.getByTitle('Editer'))

    await waitFor(() => expect(screen.getByTestId('validate-address')).toBeEnabled())
    fireEvent.click(screen.getByTestId('validate-address'))

    expect(onChange).toHaveBeenCalledTimes(1)
    expect(onChange).toHaveBeenCalledWith(completeAddress)
    expect(screen.getByTestId('address-form-item')).toHaveAttribute('data-readonly', 'true')
  })

  it('should leave edit mode without sending anything when cancelling', () => {
    render(<AddressForm onChange={onChange} address={completeAddress} />)
    fireEvent.click(screen.getByTitle('Editer'))

    fireEvent.click(screen.getByText('Annuler'))

    expect(onChange).not.toHaveBeenCalled()
    expect(screen.getByTestId('address-form-item')).toHaveAttribute('data-readonly', 'true')
  })

  it('should not submit anything on its own, even inside a surrounding form', () => {
    const onSubmit = vi.fn((e: React.FormEvent) => e.preventDefault())
    render(
      <form onSubmit={onSubmit}>
        <AddressForm onChange={onChange} address={{ town: 'Brest' }} />
      </form>
    )

    fireEvent.click(screen.getByTestId('validate-address'))

    expect(onSubmit).not.toHaveBeenCalled()
  })
})
