import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikSelectInput } from '../../../common/components/ui/formik-select-input.tsx'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input.tsx'
import { usePno } from '../../../common/hooks/use-pno.tsx'

interface JpeFormProps {
  name?: string
}

const JpeForm: FC<JpeFormProps> = ({ name }) => {
  const { pnoTypeOptions } = usePno()
  return (
    <Stack direction="row" spacing=".5rem" justifyContent={'flex-start'} style={{ width: '100%' }}>
      <Stack.Item style={{ flex: 1 }}>
        <FormikTextInput
          isRequired
          label="N° de marée"
          isLight={false}
          isErrorMessageHidden
          name={`${name}.jpe.tripNumber`}
        />
      </Stack.Item>
      <Stack.Item style={{ flex: 1 }}>
        <FormikSelectInput
          isLight={false}
          label="Objet du PNO"
          isErrorMessageHidden
          options={pnoTypeOptions}
          name={`${name}.jpe.pnoType`}
        />
      </Stack.Item>
    </Stack>
  )
}
export default JpeForm
