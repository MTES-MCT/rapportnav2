import { FormikSelect } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import { AdminActionType } from '../../types/admin-action.ts'
import { Agent, AgentRole } from '../../types/admin-agent-types.ts'

interface AdminAgentServiceProps {
  agents: Agent[]
  roles: AgentRole[]
  disabledAgents: number[]
  formik: FormikProps<any>
  type: AdminActionType
}

const AdminAgentServiceForm: React.FC<AdminAgentServiceProps> = ({ type, formik, roles, agents, disabledAgents }) => {
  return (
    <Stack.Item style={{ width: '100%' }}>
      {roles && agents && disabledAgents && (
        <Stack direction="row" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
          <Stack.Item style={{ flex: 1, width: '50%' }}>
            <FormikSelect
              name="agentId"
              label="Identité"
              isRequired={true}
              options={
                agents?.map(({ id, firstName, lastName }) => ({
                  value: id!!,
                  label: `${firstName} ${lastName}`
                })) ?? []
              }
              searchable
              disabledItemValues={disabledAgents}
              disabled={type === AdminActionType.UPDATE}
            />
          </Stack.Item>
          <Stack.Item style={{ flex: 1, width: '50%' }}>
            <FormikSelect
              name="roleId"
              label="Role"
              isRequired={true}
              options={roles?.map(({ id, title }) => ({ value: id, label: title }))}
            />
          </Stack.Item>
        </Stack>
      )}
    </Stack.Item>
  )
}

export default AdminAgentServiceForm
