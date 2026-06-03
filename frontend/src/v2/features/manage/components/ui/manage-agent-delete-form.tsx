import { Label } from '@mtes-mct/monitor-ui'
import type { FormikProps } from 'formik'
import type { FunctionComponent } from 'react'
import { Divider, Stack } from 'rsuite'

const ManageAgentDeleteForm: FunctionComponent<{ formik: FormikProps<any> }> = ({ formik }) => (
  <Stack direction="row" style={{ height: '100%' }}>
    <Stack.Item style={{ width: '50%' }}>
      <Stack direction="column" alignItems="flex-start">
        <Stack.Item>
          <Label>Prénom, nom</Label>
        </Stack.Item>
        <Stack.Item>{formik.values.name}</Stack.Item>
      </Stack>
    </Stack.Item>

    <Stack.Item style={{ height: '100%' }}>
      <Divider vertical style={{ height: '100%' }} />
    </Stack.Item>

    <Stack.Item>
      <Stack direction="column" alignItems="flex-start">
        <Stack.Item>
          <Label>Rôle</Label>
        </Stack.Item>
        <Stack.Item>{formik.values.role}</Stack.Item>
      </Stack>
    </Stack.Item>
  </Stack>
)

export default ManageAgentDeleteForm
