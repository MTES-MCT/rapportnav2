import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikSelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { StyledFormikTextInput } from '../../../common/components/ui/formik-text-input.tsx'
import { useCountry } from '../../../common/hooks/use-countries.tsx'

interface AddressFormProps {
  name: string
  disabled?: boolean
  readOnly?: boolean
}

export const AddressForm: FC<AddressFormProps> = ({ name, disabled, readOnly }) => {
  const { countries } = useCountry()
  return (
    <>
      <Stack.Item style={{ width: '100%' }}>
        <StyledFormikTextInput
          name={`${name}.street`}
          label="Adresse (n°, type, nom de la voie)"
          isLight={readOnly}
          readOnly={readOnly}
          disabled={disabled}
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" style={{ width: '100%' }}>
          <Stack.Item style={{ flex: 1 }}>
            <StyledFormikTextInput
              name={`${name}.zipcode`}
              label="Code postal"
              isLight={readOnly}
              readOnly={readOnly}
              disabled={disabled}
            />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <StyledFormikTextInput
              name={`${name}.town`}
              label="Commune"
              isLight={readOnly}
              readOnly={readOnly}
              disabled={disabled}
            />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <FormikSelectInput
              isRequired
              label="Pays"
              isLight={readOnly}
              readOnly={readOnly}
              options={countries}
              isErrorMessageHidden
              name={`${name}.country`}
              disabled={disabled}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </>
  )
}
