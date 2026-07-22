import { Accent, Button, Size } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { Contact } from 'src/v2/features/common/types/sati.ts'
import { object, string } from 'yup'
import { FormikSelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { StyledFormikTextInput } from '../../../common/components/ui/formik-text-input.tsx'
import { useCountry } from '../../../common/hooks/use-countries.tsx'
import { AddressForm } from './address-form.tsx'

const schema = object().shape({
  fullName: string().required(),
  email: string().email().required(),
  phone: string(),
  address: object({
    country: string()
  })
})

const emptyContact: Contact = {
  fullName: '',
  email: '',
  phone: '',
  nationality: '',
  address: { street: '', fullAddress: '', zipcode: '', town: '', country: undefined }
}

interface ContactFormItemProps {
  contact?: Contact
  disabled?: boolean
  readOnly?: boolean
  onClose?: () => void
  onSubmit?: (value: Contact) => void
}

const ContactFormItem: FC<ContactFormItemProps> = ({ contact, onClose, disabled, onSubmit, readOnly }) => {
  const { countries } = useCountry()
  const [initValue, setInitValue] = useState<Contact>()

  const getInitValues = (baseValue?: Contact) => {
    return { ...emptyContact, ...baseValue, address: { ...emptyContact.address, ...baseValue?.address } }
  }
  const handleClose = () => {
    if (onClose) onClose()
  }
  const handleSubmit = (value?: any) => {
    if (onSubmit) onSubmit(value)
  }

  useEffect(() => {
    setInitValue(getInitValues(contact))
  }, [contact])

  return (
    <Formik
      enableReinitialize
      onSubmit={handleSubmit}
      initialValues={initValue}
      validationSchema={schema}
      validateOnChange
    >
      {({ isValid, resetForm }) => (
        <Form>
          <Stack
            direction="column"
            spacing="2rem"
            alignItems="flex-start"
            style={{ width: '100%', marginBottom: '.5rem' }}
          >
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
                          <StyledFormikTextInput
                            name={`fullName`}
                            label="Identité"
                            isLight={readOnly}
                            readOnly={readOnly}
                            disabled={disabled}
                          />
                        </Stack.Item>
                      </Stack>
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <StyledFormikTextInput
                        isRequired
                        name={`email`}
                        label="Adresse email"
                        isLight={readOnly}
                        readOnly={readOnly}
                        disabled={disabled}
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
                        disabled={disabled}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ flex: 1, width: '100%' }}>
                      <StyledFormikTextInput
                        name={`phone`}
                        isLight={readOnly}
                        readOnly={readOnly}
                        disabled={disabled}
                        label="N° de téléphone"
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <AddressForm name={`address`} readOnly={readOnly} disabled={disabled} />
            </Stack.Item>

            {!readOnly && (
              <Stack.Item style={{ width: '100%' }}>
                <Stack justifyContent="flex-end" spacing={'1rem'} style={{ width: '100%' }}>
                  <Stack.Item>
                    <Button
                      onClick={() => {
                        resetForm()
                        handleClose()
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
                      type="submit"
                      size={Size.NORMAL}
                      accent={Accent.PRIMARY}
                      role="validate-infraction"
                      disabled={disabled || !isValid}
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
