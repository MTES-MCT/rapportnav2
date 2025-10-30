import Text from '@common/components/ui/text.tsx'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import { useStore } from '@tanstack/react-store'
import React from 'react'
import { Stack, Tabs } from 'rsuite'
import AdminAgentItem from '../features/admin/components/elements/admin-agent-item.tsx'
import AdminAgentRoleItem from '../features/admin/components/elements/admin-agent-role-item.tsx'
import AdminServiceItem from '../features/admin/components/elements/admin-service-item.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper.tsx'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper.tsx'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { store } from '../store/index.ts'

const AdminPage: React.FC = () => {
  const { getSidebarItems } = useGlobalRoutes()
  const user = useStore(store, state => state.user)

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="admin" items={getSidebarItems()} />}
      footer={undefined}
    >
      <Stack direction={'column'} style={{ width: '100%' }}>
        <Stack.Item style={{ width: '100%' }}>
          <Stack
            direction={'row'}
            spacing={'0.2rem'}
            alignItems={'center'}
            style={{ padding: '0 11.5rem', marginTop: '3rem' }}
          >
            <Stack.Item>
              <Text as={'h1'} style={{ fontSize: '32px' }}>
                Dashboard
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack
            direction="column"
            alignItems="flex-start"
            spacing="0.2rem"
            style={{ padding: '0 11.5rem', marginTop: '3rem' }}
          >
            <Stack.Item style={{ width: '100%', height: '100%' }}>
              <Tabs defaultActiveKey="1" vertical appearance="subtle">
                <Tabs.Tab eventKey="1" title="Services">
                  <AdminServiceItem />
                </Tabs.Tab>
                <Tabs.Tab eventKey="2" title="Agent roles">
                  <AdminAgentRoleItem />
                </Tabs.Tab>
                <Tabs.Tab eventKey="3" title="Agents">
                  <AdminAgentItem />
                </Tabs.Tab>
              </Tabs>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    </MissionListPageWrapper>
  )
}

export default AdminPage
