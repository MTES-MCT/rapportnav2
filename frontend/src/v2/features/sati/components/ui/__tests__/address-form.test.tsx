import { Formik } from 'formik'
import { render, screen } from '../../../../../../test-utils'
import { AddressForm } from '../address-form'

const renderForm = (address: Record<string, unknown>, readOnly?: boolean) =>
  render(
    <Formik initialValues={{ address }} onSubmit={() => {}}>
      <AddressForm name="address" readOnly={readOnly} />
    </Formik>
  )

describe('AddressForm', () => {
  it('should hide the zipcode/town/country row when read-only and all three are empty', () => {
    renderForm({ street: '12 Quai des Chartrons' }, true)

    expect(screen.getByLabelText('Adresse (n°, type, nom de la voie)')).toBeInTheDocument()
    expect(screen.queryByLabelText('Code postal')).not.toBeInTheDocument()
    expect(screen.queryByLabelText('Commune')).not.toBeInTheDocument()
    expect(screen.queryByText('Pays')).not.toBeInTheDocument()
  })

  it('should show the row when read-only but at least one of the three has a value', () => {
    renderForm({ street: '12 Quai des Chartrons', town: 'Bordeaux' }, true)

    expect(screen.getByLabelText('Code postal')).toBeInTheDocument()
    expect(screen.getByLabelText('Commune')).toBeInTheDocument()
    expect(screen.getByText('Pays')).toBeInTheDocument()
  })

  it('should always show the row when not read-only, even if all three are empty, so they can be filled in', () => {
    renderForm({ street: '' }, false)

    expect(screen.getByLabelText('Code postal')).toBeInTheDocument()
    expect(screen.getByLabelText('Commune')).toBeInTheDocument()
    expect(screen.getByText('Pays')).toBeInTheDocument()
  })
})
