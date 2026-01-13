import { FormikNumberInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'

const AdminGeneralInfosForm: FC<{ formik: FormikProps<any> }> = ({ formik }) => {
  return (
    <Stack direction="row" spacing={'.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
        <FormikNumberInput name="service.id" label="Id du service" isErrorMessageHidden={true} />
      </Stack.Item>
    </Stack>
  )
}
export default AdminGeneralInfosForm
