import { FormikDatePicker } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { FormikSelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { StyledFormikTextInput } from '../../../common/components/ui/formik-text-input.tsx'
import { usePno } from '../../../common/hooks/use-pno.tsx'
import { usePortListAllQuery } from '../../../common/services/use-port-service.tsx'

interface JpeSummaryProps {
  name?: string
}

const JpeSummary: FC<JpeSummaryProps> = ({ name }) => {
  const { pnoTypeOptions } = usePno()
  const { data: ports } = usePortListAllQuery()

  return (
    <Stack direction="column" spacing=".5rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" style={{ width: '100%' }} justifyContent="flex-start" alignItems="center">
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
          <Stack.Item style={{ flex: 1, paddingTop: 10 }}>
            <FormikSelectInput
              isLight={true}
              readOnly={true}
              isRequired={false}
              label="Objet du PNO"
              options={pnoTypeOptions}
              name={`${name}.jpe.pnoType`}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Divider style={{ width: '100%', margin: 0 }} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" style={{ width: '100%' }} justifyContent="flex-start">
          <Stack.Item>
            <FormikSelectInput
              name={`${name}.jpe.portId`}
              label="Dernier port d’escale"
              isLight={true}
              readOnly={true}
              options={
                ports?.map(p => ({
                  value: p.locode,
                  label: `${p.name} (${p.locode})`
                })) ?? []
              }
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
