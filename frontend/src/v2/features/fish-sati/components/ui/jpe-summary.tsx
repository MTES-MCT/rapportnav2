import { FormikDatePicker } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { StyledFormikTextInput } from '../../../common/components/ui/formik-text-input.tsx'

interface JpeSummaryProps {
  name?: string
}

const JpeSummary: FC<JpeSummaryProps> = ({ name }) => {
  return (
    <Stack direction="column" spacing=".5rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" style={{ width: '100%' }} justifyContent="flex-start">
          <Stack.Item style={{ flex: 1 }}>
            <StyledFormikTextInput name={`${name}.jpe.tripNumber`} label="N° de marée" isLight={true} readOnly={true} />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <StyledFormikTextInput
              name={`${name}.jpe.pnoId`}
              label="Identifiant du PNO"
              isLight={true}
              readOnly={true}
            />
          </Stack.Item>
          <Stack.Item style={{ flex: 1 }}>
            <StyledFormikTextInput name={`${name}.jpe.pnoType`} label="Objet du PNO" isLight={true} readOnly={true} />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Divider style={{ width: '100%', margin: 0 }} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" style={{ width: '100%' }} justifyContent="flex-start">
          <Stack.Item>
            <StyledFormikTextInput
              name={`${name}.jpe.portId`}
              label="Dernier port d’escale"
              isLight={true}
              readOnly={true}
            />
          </Stack.Item>
          <Stack.Item>
            <FormikDatePicker
              isLight={true}
              readOnly={true}
              label="Date de l’escale"
              name={`${name}.jpe.lastStopDate`}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}
export default JpeSummary
