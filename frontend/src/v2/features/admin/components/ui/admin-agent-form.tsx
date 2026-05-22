import { FormikSelect, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import useGetAgentRoles from '../../../common/services/use-agent-roles.tsx'
import { AdminActionType } from '../../../common/types/basic-action.ts'
import useAdminServiceListQuery from '../../services/use-admin-services-service.tsx'

interface AdminAgentProps {
  type: AdminActionType
  formik: FormikProps<any>
}

const AdminAgentForm: React.FC<AdminAgentProps> = ({ type, formik }) => {
  const { data: roles } = useGetAgentRoles()
  const { data: services } = useAdminServiceListQuery()
  return (
    <Stack.Item style={{ width: '100%' }}>
      <Stack direction="row" spacing={'.5rem'}>
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput
            label="Prénom"
            name="firstName"
            isRequired={true}
            isErrorMessageHidden={true}
            disabled={type === AdminActionType.MIGRATE_AGENT}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput
            label="Nom"
            name="lastName"
            isRequired={true}
            isErrorMessageHidden={true}
            disabled={type === AdminActionType.MIGRATE_AGENT}
          />
        </Stack.Item>
      </Stack>
      <Stack direction="row" alignItems="flex-start" spacing=".5rem" style={{ width: '100%', marginTop: '1rem' }}>
        <Stack.Item style={{ flex: 1, width: '50%' }}>
          <FormikSelect
            name="roleId"
            label="Role"
            isRequired={true}
            isErrorMessageHidden={true}
            options={roles?.map(({ id, title }) => ({ value: id, label: title })) ?? []}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '50%' }}>
          <FormikTextInput
            label="N° de carte de service"
            name="cardId"
            isErrorMessageHidden={true}
            disabled={type === AdminActionType.MIGRATE_AGENT}
          />
        </Stack.Item>
      </Stack>
      <Stack direction="row" spacing={'.5rem'} style={{ width: '100%', marginTop: '1rem' }}>
        <Stack.Item style={{ width: '100%' }}>
          <FormikSelect
            name="serviceId"
            isRequired={true}
            label="Service"
            disabled={type === AdminActionType.UPDATE}
            options={services?.filter(s => !s.deletedAt).map(({ id, name }) => ({ value: id, label: name })) ?? []}
          />
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}

export default AdminAgentForm
