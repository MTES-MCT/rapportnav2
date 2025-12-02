import { FormikSearchProps, FormikTextInput, Message } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import styled from 'styled-components'

type FormikForeignEstablishmentProps = {
  name: string
}

export const FormikForeignEstablishment = styled(
  ({ name, ...props }: Omit<FormikSearchProps, 'options'> & FormikForeignEstablishmentProps) => {
    return (
      <Stack direction="column" spacing="0.5rem">
        <Stack.Item style={{ width: '100%' }}>
          <Message level="INFO">
            Entrer les informations d'un établissement étranger, <br />
            numéro de SIREN ou adresse dans le champs libre ci-dessous
          </Message>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column">
            <Stack.Item style={{ width: '100%' }}>
              <FormikTextInput {...props} name={name} placeholder="" isRequired={true} isErrorMessageHidden={true} />
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    )
  }
)(() => ({}))
