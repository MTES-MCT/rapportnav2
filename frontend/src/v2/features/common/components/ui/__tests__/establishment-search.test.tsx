import { fireEvent, render, screen } from '../../../../../../test-utils'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { useEstablishmentListQuery } from '../../../services/use-etablishments-service'
import { Establishment } from '../../../types/etablishment'
import { EstablishmentSearch } from '../establishment-search'

vi.mock('../../../services/use-etablishments-service', () => ({ useEstablishmentListQuery: vi.fn() }))

const withAddress: Establishment = {
  id: '1',
  name: 'ACME',
  siren: '123456789',
  siret: '12345678900011',
  address: '1 rue X',
  isHeadquarter: true
}
const withoutAddress: Establishment = { id: '2', name: 'BETA', siren: '987654321', siret: '98765432100022' }
const withoutSiret: Establishment = { id: '3', name: 'GAMMA', siren: '111222333' }

const mockList = (data?: Establishment[]) =>
  vi.mocked(useEstablishmentListQuery).mockReturnValue({ data } as ReturnType<typeof useEstablishmentListQuery>)

describe('EstablishmentSearch', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mockList(undefined)
  })

  it('should render the search help message', () => {
    render(<EstablishmentSearch name="search" label="Etablissement" handleSubmit={vi.fn()} />)

    expect(screen.getByText('Recherche dans l’annuaire entreprise de l’Etat.')).toBeInTheDocument()
    expect(screen.getByText('Le SIRET fait foi')).toBeInTheDocument()
    expect(screen.getByTestId('search-establishment')).toBeInTheDocument()
  })

  it('should show no results while the list is empty', () => {
    mockList([])
    render(<EstablishmentSearch name="search" label="Etablissement" handleSubmit={vi.fn()} />)

    expect(screen.queryByRole('menuitem')).not.toBeInTheDocument()
  })

  it('should list the establishments with name, headquarters mark, siret and address', () => {
    mockList([withAddress])
    render(<EstablishmentSearch name="search" label="Etablissement" handleSubmit={vi.fn()} />)

    expect(screen.getByText('ACME (siège) | SIRET=12345678900011 | 1 rue X')).toBeInTheDocument()
  })

  it('should omit the address part when the establishment has no address', () => {
    mockList([withoutAddress])
    render(<EstablishmentSearch name="search" label="Etablissement" handleSubmit={vi.fn()} />)

    expect(screen.getByText('BETA | SIRET=98765432100022')).toBeInTheDocument()
  })

  it('should omit the siret part when the establishment has no siret', () => {
    mockList([withoutSiret])
    render(<EstablishmentSearch name="search" label="Etablissement" handleSubmit={vi.fn()} />)

    expect(screen.getByText('GAMMA')).toBeInTheDocument()
    expect(screen.queryByText(/SIRET=/)).not.toBeInTheDocument()
  })

  it('should submit the selected establishment keeping the current id', () => {
    const handleSubmit = vi.fn()
    mockList([withAddress, withoutAddress])
    render(
      <EstablishmentSearch
        name="search"
        label="Etablissement"
        handleSubmit={handleSubmit}
        establishment={{ id: '99' }}
      />
    )

    fireEvent.click(screen.getByText('BETA | SIRET=98765432100022'))

    expect(handleSubmit).toHaveBeenCalledWith({ ...withoutAddress, id: '2' })
  })

  it('should close the list once an establishment is selected', () => {
    mockList([withAddress])
    render(<EstablishmentSearch name="search" label="Etablissement" handleSubmit={vi.fn()} />)

    fireEvent.click(screen.getByText('ACME (siège) | SIRET=12345678900011 | 1 rue X'))

    expect(screen.queryByRole('menuitem')).not.toBeInTheDocument()
  })

  it('should prefill the search with the current establishment when it has a siren', () => {
    render(
      <EstablishmentSearch name="search" label="Etablissement" handleSubmit={vi.fn()} establishment={withAddress} />
    )

    expect(screen.getByDisplayValue('ACME (siège) | SIRET=12345678900011 | 1 rue X')).toBeInTheDocument()
  })

  it('should leave the search empty when the establishment has no siren', () => {
    render(
      <EstablishmentSearch
        name="search"
        label="Etablissement"
        handleSubmit={vi.fn()}
        establishment={{ name: 'NoSiren' }}
      />
    )

    expect(screen.queryByDisplayValue(/NoSiren/)).not.toBeInTheDocument()
  })

  it('should query the list with the typed search', () => {
    render(
      <EstablishmentSearch name="search" label="Etablissement" handleSubmit={vi.fn()} establishment={withAddress} />
    )

    expect(useEstablishmentListQuery).toHaveBeenLastCalledWith('ACME (siège)  | SIRET=12345678900011 | 1 rue X')
  })
})
