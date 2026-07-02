import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikSelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input.tsx'
import { useCountry } from '../../../common/hooks/use-countries.tsx'

interface AddressFormProps {
  name: string
  readOnly?: boolean
}

export const AddressForm: FC<AddressFormProps> = ({ name, readOnly }) => {
  const { countries } = useCountry()
  return (
    <>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput
          name={`${name}.street`}
          label="Adresse (n°, type, nom de la voie)"
          isLight={readOnly}
          readOnly={readOnly}
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" style={{ width: '100%' }}>
          <Stack.Item style={{ flex: 1 }}>
            <FormikTextInput name={`${name}.zipcode`} label="Code postal" isLight={readOnly} readOnly={readOnly} />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <FormikTextInput name={`${name}.town`} label="Commune" isLight={readOnly} readOnly={readOnly} />
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
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </>
  )
}
