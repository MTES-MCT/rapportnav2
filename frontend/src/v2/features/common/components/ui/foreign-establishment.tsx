import { FormikSearchProps, Message, TextInput } from '@mtes-mct/monitor-ui'
import { FormikErrors } from 'formik'
import { isEmpty } from 'lodash'
import { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { useDelay } from '../../hooks/use-delay'
import { Establishment } from '../../types/etablishment'

type ForeignEstablishmentProps = {
  establishment?: Establishment
  handleSubmit: (value?: Establishment, errors?: FormikErrors<Establishment>) => void
}

export const ForeignEstablishment = styled(
  ({ establishment, handleSubmit, ...props }: Omit<FormikSearchProps, 'options'> & ForeignEstablishmentProps) => {
    const { handleExecuteOnDelay } = useDelay()
    const [value, setValue] = useState<string>()

    const onChange = (v?: string) => {
      if (!v) return
      setValue(v)
      handleExecuteOnDelay(async () => {
        handleSubmit({ ...establishment, name: v })
      }, 3000)
    }

    useEffect(() => {
      if (!establishment || isEmpty(establishment)) return
      setValue(establishment?.name)
    }, [establishment])

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
                value={value}
                placeholder=""
                isRequired={true}
                name="establishment"
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
