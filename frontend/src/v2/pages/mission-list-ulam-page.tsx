import { Icon } from '@mtes-mct/monitor-ui'
import React from 'react'
import MissionListUlam from '../../v2/features/ulam/components/element/mission-list-ulam'
import MissionListUlamAction from '../../v2/features/ulam/components/ui/mission-list-ulam-action'
import MissionListUlamTitle from '../../v2/features/ulam/components/ui/mission-list-ulam-title'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/layout/mission-list-page-sidebar-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'

const SIDEBAR_ITEMS = [
  {
    url: '',
    key: 'list',
    icon: Icon.MissionAction
  }
]

const MissionListUlamPage: React.FC = () => {
  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListUlamTitle />} actions={<MissionListUlamAction />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="list" items={SIDEBAR_ITEMS} />}
      footer={<></>}
    >
      <MissionListUlam />
    </MissionListPageWrapper>
  )
}

export default MissionListUlamPage
