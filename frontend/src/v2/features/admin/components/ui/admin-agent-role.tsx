import { FormikNumberInput, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'

const AdminAgentRoleForm: FC<{ formik: FormikProps<any> }> = ({ formik }) => {
  return (
    <Stack direction="row" spacing={'.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput name="title" isRequired={true} label="Titre" isErrorMessageHidden={true} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <FormikNumberInput name="priority" isRequired={true} label="Priorité" isErrorMessageHidden={true} />
      </Stack.Item>
    </Stack>
  )
}
export default AdminAgentRoleForm
