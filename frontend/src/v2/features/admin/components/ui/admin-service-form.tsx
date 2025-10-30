import { FormikNumberInput, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'

const AdminServiceForm: FC<{ formik: FormikProps<any> }> = ({ formik }) => {
  return (
    <Stack direction="row" spacing={'.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput name="name" isRequired={true} label="Nom du service" isErrorMessageHidden={true} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FormikNumberInput name="controlUnits[0]" label="Unités de contrôles" isErrorMessageHidden={true} />
      </Stack.Item>
    </Stack>
  )
}
export default AdminServiceForm
