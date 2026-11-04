import { Message, TextInput, TextInputProps } from '@mtes-mct/monitor-ui'
import { ReactNode } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { useDelayFormik } from '../../hooks/use-delay-formik'

type EstablishmentFormNameProps = {
  initValue?: string
  message?: ReactNode
  handleSubmit: (value?: string) => void
}

export const EstablishmentFormName = styled(
  ({ initValue, message, handleSubmit, ...props }: Omit<TextInputProps, 'options'> & EstablishmentFormNameProps) => {
    const { value, onChange } = useDelayFormik(initValue, (v?: string | number) => handleSubmit(v as string), 3000)

    return (
      <Stack direction="column" spacing="0.5rem">
        {message && (
          <Stack.Item style={{ width: '100%' }}>
            <Message level="INFO">{message}</Message>
          </Stack.Item>
        )}
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column">
            <Stack.Item style={{ width: '100%' }}>
              <TextInput
                {...props}
                name="name"
                placeholder=""
                isRequired={true}
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
