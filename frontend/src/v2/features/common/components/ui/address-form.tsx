import { Accent, Button, Size } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { object, string } from 'yup'
import { AddressFormItem } from '../../../sati/components/ui/address-form-item.tsx'
import { Address } from '../../types/sati.ts'
import PanelEditLayout from '../layout/panel-edit-layout.tsx'

const notBlank = (value?: string) => !!value?.trim()
const emptyAddress: Address = {} as Address

const schema = object().shape({
  address: object({
    town: string().required().test('not-blank', '', notBlank),
    street: string().required().test('not-blank', '', notBlank),
    country: string().required().test('not-blank', '', notBlank),
    zipcode: string().required().test('not-blank', '', notBlank)
  })
})

interface AddressFormProps {
  disabled?: boolean
  address?: Address
  onChange: (value?: Address) => void
}

const AddressForm: FC<AddressFormProps> = ({ disabled, address, onChange }) => {
  const initialValues = { address: address ?? emptyAddress }
  const handleSubmit = (values: { address: Address }) => onChange(values.address)

  return (
    <PanelEditLayout title="Adresse de l’établissement contrôlé" defaultEdit={!address?.street}>
      {({ edit, setEdit }) => (
        <Formik
          validateOnMount
          validateOnChange
          enableReinitialize
          onSubmit={handleSubmit}
          validationSchema={schema}
          initialValues={initialValues}
        >
          {({ values, isValid, resetForm, validateForm, initialValues }) => (
            <Stack
              direction="column"
              spacing="2rem"
              alignItems="flex-start"
              style={{ width: '100%', marginBottom: '.5rem' }}
            >
              <Stack.Item style={{ width: '100%' }}>
                <AddressFormItem name={`address`} readOnly={!edit} disabled={disabled} />
              </Stack.Item>

              {edit && (
                <Stack.Item style={{ width: '100%' }}>
                  <Stack justifyContent="flex-end" spacing={'1rem'} style={{ width: '100%' }}>
                    <Stack.Item>
                      <Button
                        onClick={() => {
                          resetForm()
                          validateForm(initialValues)
                          setEdit(false)
                        }}
                        size={Size.NORMAL}
                        role="cancel-infraction"
                        accent={Accent.SECONDARY}
                        disabled={disabled}
                      >
                        Annuler
                      </Button>
                    </Stack.Item>
                    <Stack.Item>
                      <Button
                        type="button"
                        onClick={() => {
                          handleSubmit(values)
                          setEdit(false)
                        }}
                        size={Size.NORMAL}
                        accent={Accent.PRIMARY}
                        role="validate-address"
                        disabled={disabled || !isValid}
                        data-testid="validate-address"
                      >
                        Valider
                      </Button>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              )}
            </Stack>
          )}
        </Formik>
      )}
    </PanelEditLayout>
  )
}

export default AddressForm
