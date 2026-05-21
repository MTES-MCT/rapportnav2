import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { useSelector } from '@tanstack/react-store'
import React from 'react'
import { useNavigate } from 'react-router-dom'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper.tsx'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import StyledTabs, { StyledTabItem } from '../features/common/components/ui/styled-tab.tsx'
import ManageCrewItem from '../features/manage/components/elements/manage-crew-item.tsx'
import ManageResourceItem from '../features/manage/components/elements/manage-resource-item.tsx'
import { store } from '../store/index.ts'

const ITEMS: StyledTabItem[] = [
  {
    key: 'crew',
    icon: Icon.GroupPerson,
    title: `Membres de l'unité`,
    component: ManageCrewItem
  },
  {
    key: 'resource',
    icon: Icon.Car,
    title: `Moyens de l'unité`,
    component: ManageResourceItem
  }
]

const ManagePage: React.FC = () => {
  const navigate = useNavigate()
  const user = useSelector(store, state => state.user)

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      footer={undefined}
    >
      <div
        key={'main-wrapper'}
        style={{
          margin: 0,
          width: '100%',
          display: 'flex',
          flexDirection: 'row',
          minHeight: '100vh',
          maxHeight: '100vh',
          alignItems: 'start',
          justifyContent: 'center'
        }}
      >
        <div
          style={{
            display: 'flex',
            width: '50%',
            padding: '1rem',
            overflowY: 'auto',
            alignItems: 'start',
            flexDirection: 'column',
            justifyContent: 'center'
          }}
        >
          <div style={{ padding: '0 3rem', marginTop: '2rem', width: '50%' }}>
            <Button
              Icon={Icon.Minus}
              accent={Accent.TERTIARY}
              onClick={() => navigate('/')}
              style={{ marginLeft: 8, marginRight: 4 }}
            >
              Retour page d'acceuil
            </Button>
            <Text as={'h1'} style={{ fontSize: '32px' }}>
              Gestion de L'unité
            </Text>
          </div>
          <div
            style={{
              padding: '0 4rem',
              marginTop: '3rem',
              height: '100%',
              width: '100%',
              display: 'flex',
              justifyContent: 'center'
            }}
          >
            <StyledTabs className="styled-tabs-manage" items={ITEMS} defaultActiveKey={ITEMS[0].key} params={user} />
          </div>
        </div>
      </div>
    </MissionListPageWrapper>
  )
}

export default ManagePage
