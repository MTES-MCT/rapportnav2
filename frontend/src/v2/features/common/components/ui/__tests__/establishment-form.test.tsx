import { fireEvent, render, screen, waitFor } from '../../../../../../test-utils'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { Establishment } from '../../../types/etablishment'
import { EstablishmentForm } from '../establishment-form'

vi.mock('../establishment-search', () => ({
  EstablishmentSearch: ({
    establishment,
    handleSubmit,
    disabled
  }: {
    establishment?: Establishment
    handleSubmit: (v?: Establishment) => void
    disabled?: boolean
  }) => (
    <div data-testid="search" data-disabled={String(!!disabled)} data-name={establishment?.name ?? ''}>
      <button onClick={() => handleSubmit({ id: '5', name: 'PICKED', siren: '555', siret: '55500011' })}>pick</button>
    </div>
  )
}))

vi.mock('../establishment-form-name', () => ({
  EstablishmentFormName: ({ initValue, handleSubmit }: { initValue?: string; handleSubmit: (v?: string) => void }) => (
    <div data-testid="name-input" data-value={initValue ?? ''}>
      <button onClick={() => handleSubmit('Typed name')}>type</button>
    </div>
  )
}))

vi.mock('../address-form', () => ({
  default: ({ address, onChange }: { address?: { street?: string }; onChange: (a: object) => void }) => (
    <div data-testid="address-form" data-street={address?.street ?? ''}>
      <button onClick={() => onChange({ street: '2 rue neuve', town: 'Nantes', zipcode: '44000', country: 'FRA' })}>
        edit-address
      </button>
    </div>
  )
}))

const company: Establishment = {
  id: '13',
  name: 'FOE',
  siren: '513691477',
  siret: '51369147700014',
  address: '12 Quai des Chartrons',
  city: 'Bordeaux',
  zipcode: '33000',
  country: 'FRA'
}

const NOT_LISTED = 'Etablissement non présent dans la liste'
const NOT_SAME_ADDRESS = 'L’adresse de contrôle est différente du siège social de l’établissement'

const lastChange = (onChange: ReturnType<typeof vi.fn>) => onChange.mock.calls.at(-1)?.[0] as Establishment

describe('EstablishmentForm', () => {
  const onChange = vi.fn().mockResolvedValue(undefined)

  beforeEach(() => {
    vi.clearAllMocks()
    onChange.mockResolvedValue(undefined)
  })

  it('should show the search with the establishment when it is listed', async () => {
    render(<EstablishmentForm name="establishment" establishment={company} onChange={onChange} />)

    expect(await screen.findByTestId('search')).toHaveAttribute('data-name', 'FOE')
    expect(screen.queryByTestId('name-input')).not.toBeInTheDocument()
  })

  it('should show the free name input when the establishment is not listed', async () => {
    render(
      <EstablishmentForm name="establishment" establishment={{ ...company, isForeign: true }} onChange={onChange} />
    )

    expect(await screen.findByTestId('name-input')).toHaveAttribute('data-value', 'FOE')
    expect(screen.queryByTestId('search')).not.toBeInTheDocument()
  })

  it('should not send any change on mount', async () => {
    render(<EstablishmentForm name="establishment" establishment={company} onChange={onChange} />)
    await screen.findByTestId('search')

    expect(onChange).not.toHaveBeenCalled()
  })

  it('should send the establishment picked in the search', async () => {
    render(<EstablishmentForm name="establishment" establishment={company} onChange={onChange} />)

    fireEvent.click(await screen.findByText('pick'))

    await waitFor(() => expect(onChange).toHaveBeenCalled())
    expect(lastChange(onChange)).toMatchObject({ id: '5', name: 'PICKED', siren: '555', siret: '55500011' })
  })

  describe('establishment not listed', () => {
    it('should ask for a confirmation instead of switching right away', async () => {
      render(<EstablishmentForm name="establishment" establishment={company} onChange={onChange} />)

      fireEvent.click(await screen.findByLabelText(NOT_LISTED))

      expect(await screen.findByText('Non, je recherche à nouveau')).toBeInTheDocument()
      expect(screen.getByTestId('search')).toHaveAttribute('data-disabled', 'true')
      expect(screen.getByTestId('search')).toBeInTheDocument()
      expect(onChange).not.toHaveBeenCalled()
    })

    it('should go back to the search when answering no', async () => {
      render(<EstablishmentForm name="establishment" establishment={company} onChange={onChange} />)
      fireEvent.click(await screen.findByLabelText(NOT_LISTED))

      fireEvent.click(await screen.findByText('Non, je recherche à nouveau'))

      await waitFor(() => expect(screen.queryByText('Non, je recherche à nouveau')).not.toBeInTheDocument())
      expect(screen.getByTestId('search')).toHaveAttribute('data-disabled', 'false')
      expect(onChange).not.toHaveBeenCalled()
    })

    it('should switch to the free name input and clear the name when answering yes', async () => {
      render(<EstablishmentForm name="establishment" establishment={company} onChange={onChange} />)
      fireEvent.click(await screen.findByLabelText(NOT_LISTED))

      fireEvent.click(await screen.findByText('Oui, je dois ajouter l’établissement manuellement'))

      expect(await screen.findByTestId('name-input')).toHaveAttribute('data-value', '')
      expect(screen.queryByText('Non, je recherche à nouveau')).not.toBeInTheDocument()
      await waitFor(() => expect(onChange).toHaveBeenCalled())
      expect(lastChange(onChange)).toMatchObject({ id: '13', isForeign: true, name: '' })
    })

    it('should clear the company and go back to the search when unchecking', async () => {
      render(
        <EstablishmentForm name="establishment" establishment={{ ...company, isForeign: true }} onChange={onChange} />
      )

      fireEvent.click(await screen.findByLabelText(NOT_LISTED))

      expect(await screen.findByTestId('search')).toBeInTheDocument()
      await waitFor(() => expect(onChange).toHaveBeenCalled())
      const sent = lastChange(onChange)
      expect(sent.name).toBeUndefined()
      expect(sent.siren).toBeUndefined()
      expect(sent.siret).toBeUndefined()
      expect(sent.isForeign).toBeFalsy()
      expect(sent.address).toBe('12 Quai des Chartrons')
    })

    it('should send the typed name', async () => {
      render(
        <EstablishmentForm name="establishment" establishment={{ ...company, isForeign: true }} onChange={onChange} />
      )

      fireEvent.click(await screen.findByText('type'))

      await waitFor(() => expect(onChange).toHaveBeenCalled())
      expect(lastChange(onChange)).toMatchObject({ id: '13', name: 'Typed name', isForeign: true })
    })
  })

  describe('control address', () => {
    it('should not show the address form while the address is the head office one', async () => {
      render(<EstablishmentForm name="establishment" establishment={company} onChange={onChange} />)
      await screen.findByTestId('search')

      expect(screen.queryByTestId('address-form')).not.toBeInTheDocument()
    })

    it('should show the address form and clear the address when the control address differs', async () => {
      render(<EstablishmentForm name="establishment" establishment={company} onChange={onChange} />)

      fireEvent.click(await screen.findByLabelText(NOT_SAME_ADDRESS))

      expect(await screen.findByTestId('address-form')).toHaveAttribute('data-street', '')
      await waitFor(() => expect(onChange).toHaveBeenCalled())
      const sent = lastChange(onChange)
      expect(sent.isNotSameAddress).toBe(true)
      expect(sent.address).toBeUndefined()
      expect(sent.city).toBeUndefined()
      expect(sent.name).toBe('FOE')
    })

    it('should give the establishment address to the address form', async () => {
      render(
        <EstablishmentForm
          name="establishment"
          establishment={{ ...company, isNotSameAddress: true }}
          onChange={onChange}
        />
      )

      expect(await screen.findByTestId('address-form')).toHaveAttribute('data-street', '12 Quai des Chartrons')
    })

    it('should send the address edited in the address form', async () => {
      render(
        <EstablishmentForm
          name="establishment"
          establishment={{ ...company, isNotSameAddress: true }}
          onChange={onChange}
        />
      )

      fireEvent.click(await screen.findByText('edit-address'))

      await waitFor(() => expect(onChange).toHaveBeenCalled())
      expect(lastChange(onChange)).toMatchObject({
        id: '13',
        name: 'FOE',
        isNotSameAddress: true,
        address: '2 rue neuve',
        city: 'Nantes',
        zipcode: '44000',
        country: 'FRA'
      })
    })

    it('should hide the address form when the control address is the head office one again', async () => {
      render(
        <EstablishmentForm
          name="establishment"
          establishment={{ ...company, isNotSameAddress: true }}
          onChange={onChange}
        />
      )

      fireEvent.click(await screen.findByLabelText(NOT_SAME_ADDRESS))

      await waitFor(() => expect(screen.queryByTestId('address-form')).not.toBeInTheDocument())
    })
  })
})
