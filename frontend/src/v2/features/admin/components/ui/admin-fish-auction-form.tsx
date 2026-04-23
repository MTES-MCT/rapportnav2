import { FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import { AdminActionType } from '../../types/admin-action.ts'

interface AdminFishAuctionFormProps {
  type: AdminActionType
  formik: FormikProps<any>
}

const AdminFishAuctionForm: React.FC<AdminFishAuctionFormProps> = ({ type, formik }) => {
  return (
    <Stack.Item style={{ width: '100%' }}>
      <Stack direction="row" spacing={'.5rem'}>
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput label="Nom" name="name" isRequired={true} isErrorMessageHidden={true} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput label="Façade" name="facade" isRequired={true} isErrorMessageHidden={true} />
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}

export default AdminFishAuctionForm
