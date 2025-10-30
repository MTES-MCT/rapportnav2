import { FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'

const AdminAgentForm: FC<{ formik: FormikProps<any> }> = ({ formik }) => {
  return (
    <Stack direction="row" spacing={'.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput name="firstName" isRequired={true} label="Prénom" isErrorMessageHidden={true} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput name="lastName" isRequired={true} label="Nom" isErrorMessageHidden={true} />
      </Stack.Item>
    </Stack>
  )
}
export default AdminAgentForm
