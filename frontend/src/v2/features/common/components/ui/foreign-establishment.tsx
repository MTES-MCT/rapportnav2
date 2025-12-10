import { FormikSearchProps, Message, TextInput } from '@mtes-mct/monitor-ui'
import { FormikErrors } from 'formik'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { useDelayFormik } from '../../hooks/use-delay-formik'
import { Establishment } from '../../types/etablishment'

type ForeignEstablishmentProps = {
  establishment?: Establishment
  handleSubmit: (value?: Establishment, errors?: FormikErrors<Establishment>) => void
}

export const ForeignEstablishment = styled(
  ({ establishment, handleSubmit, ...props }: Omit<FormikSearchProps, 'options'> & ForeignEstablishmentProps) => {
    const { value, onChange } = useDelayFormik(
      establishment?.name,
      (v?: string | number) => handleSubmit({ ...establishment, name: v as string }),
      3000
    )

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
              <TextInput
                {...props}
                placeholder=""
                isRequired={true}
                name="establishment"
                value={value as string}
                isErrorMessageHidden={true}
                onChange={(v?: string) => onChange(v)}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    )
  }
)(() => ({}))
