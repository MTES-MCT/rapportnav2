import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { useSelector } from '@tanstack/react-store'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FlexboxGrid, Stack, Tabs } from 'rsuite'
import AdminAgentItem from '../features/admin/components/elements/admin-agent-item.tsx'
import AdminAgentRoleItem from '../features/admin/components/elements/admin-agent-role-item.tsx'
import AdminApikeyItem from '../features/admin/components/elements/admin-apikey-item.tsx'
import AdminFishAuctionItem from '../features/admin/components/elements/admin-fish-auction-item.tsx'
import AdminGeneralInfosItem from '../features/admin/components/elements/admin-general-infos-item.tsx'
import AdminMissionItem from '../features/admin/components/elements/admin-mission-item.tsx'
import AdminServiceItem from '../features/admin/components/elements/admin-service-item.tsx'
import AdminApiKeyLoggingItem from '../features/admin/components/elements/admin-user-apikey-logging-item.tsx'
import AdminUserAuthLoggingItem from '../features/admin/components/elements/admin-user-auth-logging-item.tsx'
import AdminUserItem from '../features/admin/components/elements/admin-user-item.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper.tsx'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { store } from '../store'

const AdminPage: React.FC = () => {
  const navigate = useNavigate()
  const user = useSelector(store, state => state.user)
  const [activeKey, setActiveKey] = useState<number>(1)

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
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
        <div style={{ padding: '0 3rem', marginTop: '2rem' }}>
          <Button
            Icon={Icon.Minus}
            accent={Accent.TERTIARY}
            onClick={() => navigate('/')}
            style={{ marginLeft: 8, marginRight: 4 }}
          >
            Retour page d'accueil
          </Button>
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
                padding: '1rem',
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
                    <Tabs.Tab eventKey="1" title="Agents">
                      <AdminAgentItem />
                    </Tabs.Tab>
                    <Tabs.Tab eventKey="2" title="Roles">
                      <AdminAgentRoleItem />
                    </Tabs.Tab>
                    <Tabs.Tab eventKey="3" title="Services">
                      <AdminServiceItem />
                    </Tabs.Tab>
                    <Tabs.Tab eventKey="4" title="Users">
                      <AdminUserItem />
                    </Tabs.Tab>
                    __
                    <Tabs.Tab eventKey="5" title="Missions">
                      <AdminMissionItem />
                    </Tabs.Tab>
                    <Tabs.Tab eventKey="6" title="General Infos">
                      <AdminGeneralInfosItem />
                    </Tabs.Tab>
                    __
                    <Tabs.Tab eventKey="7" title="Criées">
                      <AdminFishAuctionItem />
                    </Tabs.Tab>
                    __
                    <Tabs.Tab eventKey="8" title="API Keys">
                      <AdminApikeyItem />
                    </Tabs.Tab>
                    __
                    <Tabs.Tab eventKey="9" title="User Auth logs">
                      <AdminUserAuthLoggingItem />
                    </Tabs.Tab>
                    <Tabs.Tab eventKey="10" title="API Keys logs">
                      <AdminApiKeyLoggingItem />
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
