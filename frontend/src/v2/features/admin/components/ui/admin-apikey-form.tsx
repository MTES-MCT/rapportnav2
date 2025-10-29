import { FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'

const AdminApiKeyForm: FC<{ formik: FormikProps<any> }> = ({ formik }) => {
  return (
    <Stack direction="row" spacing={'.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput name="owner" isRequired={true} label="Owner" isErrorMessageHidden={true} />
      </Stack.Item>
    </Stack>
  )
}
export default AdminApiKeyForm
