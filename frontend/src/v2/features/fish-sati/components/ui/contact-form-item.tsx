import { Accent, Button, Size } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { Contact } from 'src/v2/features/common/types/sati.ts'
import { FormikSelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input.tsx'
import { useCountry } from '../../../common/hooks/use-countries.tsx'
import { AddressForm } from './address-form.tsx'

const emptyContact: Contact = {
  fullName: '',
  email: '',
  phone: '',
  nationality: '',
  address: { street: '', fullAddress: '', zipcode: '', town: '', country: undefined }
}

interface ContactFormItemProps {
  contact?: Contact
  readOnly?: boolean
  onClose?: () => void
  onSubmit?: (value: Contact) => void
}

const ContactFormItem: FC<ContactFormItemProps> = ({ contact, onClose, onSubmit, readOnly }) => {
  const { countries } = useCountry()
  const initialValues = { ...emptyContact, ...contact, address: { ...emptyContact.address, ...contact?.address } }

  const handleClose = () => {
    if (onClose) onClose()
  }
  const handleSubmit = (value?: any) => {
    if (onSubmit) onSubmit(value)
  }

  return (
    <Formik enableReinitialize onSubmit={handleSubmit} initialValues={initialValues}>
      {() => (
        <Form>
          <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
            <Stack.Item style={{ width: '100%' }}>
              <Stack direction="row" spacing="1rem" style={{ width: '100%' }}>
                <Stack.Item style={{ flex: 2 }}>
                  <Stack
                    direction="column"
                    spacing=".5rem"
                    justifyContent={'flex-start'}
                    alignItems={'flex-start'}
                    style={{ width: '100%' }}
                  >
                    <Stack.Item style={{ width: '100%' }}>
                      <Stack direction="row" spacing=".5rem" justifyContent={'flex-start'} style={{ width: '100%' }}>
                        <Stack.Item style={{ flex: 1 }}>
                          <FormikTextInput name={`fullName`} label="Identité" isLight={readOnly} readOnly={readOnly} />
                        </Stack.Item>
                      </Stack>
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <FormikTextInput
                        name={`email`}
                        label="Adresse email"
                        isRequired={false}
                        isLight={readOnly}
                        readOnly={readOnly}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ flex: 1 }}>
                  <Stack
                    direction="column"
                    spacing=".5rem"
                    justifyContent={'flex-start'}
                    alignItems={'flex-start'}
                    style={{ width: '100%' }}
                  >
                    <Stack.Item style={{ flex: 1, width: '100%' }}>
                      <FormikSelectInput
                        isRequired
                        label="Nationalité"
                        isErrorMessageHidden
                        isLight={readOnly}
                        readOnly={readOnly}
                        name={`nationality`}
                        options={countries}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ flex: 1, width: '100%' }}>
                      <FormikTextInput name={`phone`} label="N° de téléphone" isLight={readOnly} readOnly={readOnly} />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <AddressForm name={`address`} readOnly={readOnly} />
            </Stack.Item>

            {!readOnly && (
              <Stack.Item style={{ width: '100%' }}>
                <Stack justifyContent="flex-end" spacing={'1rem'} style={{ width: '100%' }}>
                  <Stack.Item>
                    <Button size={Size.NORMAL} role="cancel-infraction" accent={Accent.SECONDARY} onClick={handleClose}>
                      Annuler
                    </Button>
                  </Stack.Item>
                  <Stack.Item>
                    <Button
                      type="submit"
                      size={Size.NORMAL}
                      accent={Accent.PRIMARY}
                      role="validate-infraction"
                      data-testid="validate-infraction"
                    >
                      Valider
                    </Button>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            )}
          </Stack>
        </Form>
      )}
    </Formik>
  )
}
export default ContactFormItem
