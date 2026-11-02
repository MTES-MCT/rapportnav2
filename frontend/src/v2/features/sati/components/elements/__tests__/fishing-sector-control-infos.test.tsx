import { Formik, FormikProps, useFormikContext } from 'formik'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../../test-utils'
import { Establishment } from '../../../../common/types/etablishment'
import { MissionActionData } from '../../../../common/types/mission-action-data'
import { SatiParty } from '../../../../common/types/sati'
import { SectorFishingType } from '../../../../common/types/sector-types'
import { ActionNavControlInput } from '../../../../mission-action/types/action-type'
import FishingSectorControlInfos from '../fishing-sector-control-infos'

vi.mock('../../../../common/components/ui/establishment-form', () => ({
  EstablishmentForm: ({
    establishment,
    onChange
  }: {
    establishment?: Establishment
    onChange: (value?: Establishment) => void
  }) => (
    <div data-testid="establishment-form" data-name={establishment?.name ?? ''}>
      <button onClick={() => onChange({ id: '1', name: 'PICKED' })}>pick-establishment</button>
      <button onClick={() => onChange(undefined)}>clear-establishment</button>
    </div>
  )
}))
vi.mock('../../../../common/components/ui/formik-search-port', () => ({
  FormikSearchPort: () => <div data-testid="search-port" />
}))
vi.mock('../../../../common/components/ui/formik-select-fish-auction', () => ({
  FormikSelectFishAuction: () => <div data-testid="select-fish-auction" />
}))
vi.mock('../fishing-sector-control-infos-owner', () => ({
  default: ({ owner, onChange }: { owner?: SatiParty; onChange: (v?: SatiParty) => void }) => (
    <div data-testid="owner" data-id={owner?.id ?? ''}>
      <button onClick={() => onChange({ id: 22 })}>set-owner</button>
    </div>
  )
}))
vi.mock('../fishing-sector-control-infos-operator', () => ({
  default: ({
    operator,
    onChange,
    onDelete
  }: {
    operator?: SatiParty
    onChange: (v: SatiParty) => void
    onDelete: () => void
  }) => (
    <div data-testid="operator" data-id={operator?.id ?? ''}>
      <button onClick={() => onChange({ id: 33 })}>set-operator</button>
      <button onClick={() => onDelete()}>delete-operator</button>
    </div>
  )
}))

const Subject = () => {
  const formik = useFormikContext<ActionNavControlInput>()
  return (
    <>
      <FishingSectorControlInfos
        values={formik.values as MissionActionData}
        formik={formik as FormikProps<ActionNavControlInput>}
      />
      <pre data-testid="values">{JSON.stringify(formik.values)}</pre>
    </>
  )
}

const renderInfos = (initialValues: Record<string, unknown> = {}) =>
  render(
    <Formik initialValues={initialValues} onSubmit={vi.fn()}>
      <Subject />
    </Formik>
  )

const currentValues = () => JSON.parse(screen.getByTestId('values').textContent ?? '{}')

describe('FishingSectorControlInfos', () => {
  beforeEach(() => vi.clearAllMocks())

  describe('sector type', () => {
    it('should render the establishment type select', () => {
      renderInfos()

      expect(screen.getByText("Type d'établissement")).toBeInTheDocument()
    })

    it('should show the GMS type select only for a GMS establishment', () => {
      renderInfos({ sectorEstablishmentType: SectorFishingType.GMS })

      expect(screen.getByText('Type de GMS')).toBeInTheDocument()
    })

    it.each([SectorFishingType.RESTAURANT, SectorFishingType.FISH_AUCTION, SectorFishingType.LANDING_SITE])(
      'should not show the GMS type select for %s',
      type => {
        renderInfos({ sectorEstablishmentType: type })

        expect(screen.queryByText('Type de GMS')).not.toBeInTheDocument()
      }
    )

    it('should not show the GMS type select without a type', () => {
      renderInfos()

      expect(screen.queryByText('Type de GMS')).not.toBeInTheDocument()
    })
  })

  describe('establishment area', () => {
    it('should show the establishment form with the establishment by default', () => {
      renderInfos({ establishment: { name: 'FOE' } })

      expect(screen.getByTestId('establishment-form')).toHaveAttribute('data-name', 'FOE')
      expect(screen.queryByTestId('search-port')).not.toBeInTheDocument()
      expect(screen.queryByTestId('select-fish-auction')).not.toBeInTheDocument()
    })

    it('should show the port search for a landing site', () => {
      renderInfos({ sectorEstablishmentType: SectorFishingType.LANDING_SITE })

      expect(screen.getByTestId('search-port')).toBeInTheDocument()
      expect(screen.queryByTestId('establishment-form')).not.toBeInTheDocument()
      expect(screen.queryByTestId('select-fish-auction')).not.toBeInTheDocument()
    })

    it('should show the fish auction select for a fish auction', () => {
      renderInfos({ sectorEstablishmentType: SectorFishingType.FISH_AUCTION })

      expect(screen.getByTestId('select-fish-auction')).toBeInTheDocument()
      expect(screen.queryByTestId('establishment-form')).not.toBeInTheDocument()
      expect(screen.queryByTestId('search-port')).not.toBeInTheDocument()
    })

    it('should store the establishment sent by the establishment form', async () => {
      renderInfos()

      fireEvent.click(screen.getByText('pick-establishment'))

      await waitFor(() => expect(currentValues().establishment).toEqual({ id: '1', name: 'PICKED' }))
    })

    it('should clear the establishment when the establishment form sends nothing', async () => {
      renderInfos({ establishment: { id: '9', name: 'OLD' } })

      fireEvent.click(screen.getByText('clear-establishment'))

      await waitFor(() => expect(currentValues().establishment).toBeUndefined())
    })
  })

  describe('fields cleared by establishment mode', () => {
    it('should clear the port, fish auction, zip code and city for an establishment', async () => {
      renderInfos({
        portLocode: 'FRBES',
        fishAuction: 'auction',
        zipCode: '29200',
        city: 'Brest',
        establishment: { name: 'FOE' }
      })

      await waitFor(() => {
        const values = currentValues()
        expect(values.portLocode).toBeUndefined()
        expect(values.fishAuction).toBeUndefined()
        expect(values.zipCode).toBeUndefined()
        expect(values.city).toBeUndefined()
      })
      expect(currentValues().establishment).toEqual({ name: 'FOE' })
    })

    it('should clear the establishment and the fish auction for a landing site', async () => {
      renderInfos({
        sectorEstablishmentType: SectorFishingType.LANDING_SITE,
        portLocode: 'FRBES',
        fishAuction: 'auction',
        establishment: { name: 'FOE' }
      })

      await waitFor(() => {
        const values = currentValues()
        expect(values.establishment).toBeUndefined()
        expect(values.fishAuction).toBeUndefined()
      })
      expect(currentValues().portLocode).toBe('FRBES')
    })

    it('should clear the establishment and the port for a fish auction', async () => {
      renderInfos({
        sectorEstablishmentType: SectorFishingType.FISH_AUCTION,
        portLocode: 'FRBES',
        fishAuction: 'auction',
        establishment: { name: 'FOE' }
      })

      await waitFor(() => {
        const values = currentValues()
        expect(values.establishment).toBeUndefined()
        expect(values.portLocode).toBeUndefined()
      })
      expect(currentValues().fishAuction).toBe('auction')
    })
  })

  describe('owner and operator', () => {
    const sati = { establishment: { owner: { id: 11 }, operator: { id: 12 } } }

    it('should give the owner and the operator of the sati establishment to their sections', () => {
      renderInfos({ sati })

      expect(screen.getByTestId('owner')).toHaveAttribute('data-id', '11')
      expect(screen.getByTestId('operator')).toHaveAttribute('data-id', '12')
    })

    it('should store the owner sent by the owner section', async () => {
      renderInfos({ sati })

      fireEvent.click(screen.getByText('set-owner'))

      await waitFor(() => expect(currentValues().sati.establishment.owner).toEqual({ id: 22 }))
    })

    it('should store the operator sent by the operator section', async () => {
      renderInfos({ sati })

      fireEvent.click(screen.getByText('set-operator'))

      await waitFor(() => expect(currentValues().sati.establishment.operator).toEqual({ id: 33 }))
    })

    it('should set the operator to null when deleting it', async () => {
      renderInfos({ sati })

      fireEvent.click(screen.getByText('delete-operator'))

      await waitFor(() => expect(currentValues().sati.establishment.operator).toBeNull())
    })

    it('should render the sections without any sati', () => {
      renderInfos()

      expect(screen.getByTestId('owner')).toHaveAttribute('data-id', '')
      expect(screen.getByTestId('operator')).toHaveAttribute('data-id', '')
    })
  })
})
