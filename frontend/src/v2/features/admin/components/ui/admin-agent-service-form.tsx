import { FormikSelect } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import { Agent, AgentRole } from '../../types/admin-agent-types.ts'
import { AdminAction } from '../../types/admin-services-type.ts'

interface AdminAgentServiceProps {
  agents: Agent[]
  roles: AgentRole[]
  disabledAgents: number[]
  formik: FormikProps<any>
  action: AdminAction
}

const AdminAgentServiceForm: React.FC<AdminAgentServiceProps> = ({ action, formik, roles, agents, disabledAgents }) => {
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
              disabled={action === 'UPDATE'}
              disabledItemValues={disabledAgents}
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
