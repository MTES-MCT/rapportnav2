import Text from '@common/components/ui/text.tsx'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import { useStore } from '@tanstack/react-store'
import React, { useState } from 'react'
import { FlexboxGrid, Stack, Tabs } from 'rsuite'
import AdminAgentItem from '../features/admin/components/elements/admin-agent-item.tsx'
import AdminAgentRoleItem from '../features/admin/components/elements/admin-agent-role-item.tsx'
import AdminAgentServiceItem from '../features/admin/components/elements/admin-agent-service-item.tsx'
import AdminServiceItem from '../features/admin/components/elements/admin-service-item.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper.tsx'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper.tsx'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { store } from '../store/index.ts'

const AdminPage: React.FC = () => {
  const { getSidebarItems } = useGlobalRoutes()
  const user = useStore(store, state => state.user)
  const [activeKey, setActiveKey] = useState<number>(1)

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="admin" items={getSidebarItems()} />}
      footer={undefined}
    >
      <div
        style={{
          margin: 0,
          display: 'flex',
          minHeight: '100vh',
          maxHeight: '100vh',
          width: '100%',
          flexDirection: 'column'
        }}
      >
        <div style={{ padding: '0 4rem', marginTop: '3rem' }}>
          <Text as={'h1'} style={{ fontSize: '32px' }}>
            Dashboard
          </Text>
        </div>
        <>
          <FlexboxGrid justify="space-between" style={{ display: 'flex', flex: 1 }}>
            <FlexboxGrid.Item
              colspan={8}
              style={{
                flex: 1,
                padding: '2rem',
                height: '2000px',
                overflowY: 'hidden',
                minHeight: 'calc(100vh - 2 * 60px)',
                maxHeight: 'calc(100vh - 2 * 60px)'
              }}
            >
              <Stack
                direction="column"
                alignItems="flex-start"
                spacing="0.2rem"
                style={{ padding: '0 4rem', marginTop: '3rem', height: '100%' }}
              >
                <Stack.Item style={{ width: '100%', height: '100%' }}>
                  <Tabs
                    defaultActiveKey={activeKey.toString()}
                    vertical
                    appearance="subtle"
                    style={{ width: '100%', height: '100%' }}
                  >
                    <Tabs.Tab eventKey="1" title="Services">
                      <AdminServiceItem />
                    </Tabs.Tab>
                    <Tabs.Tab eventKey="2" title="Roles">
                      <AdminAgentRoleItem />
                    </Tabs.Tab>
                    <Tabs.Tab eventKey="3" title="Agents">
                      <AdminAgentItem />
                    </Tabs.Tab>
                    <Tabs.Tab eventKey="4" title="Crews">
                      <AdminAgentServiceItem />
                    </Tabs.Tab>
                  </Tabs>
                </Stack.Item>
              </Stack>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </>
      </div>
    </MissionListPageWrapper>
  )
}

export default AdminPage
