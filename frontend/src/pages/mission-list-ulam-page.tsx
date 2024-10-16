import MissionListUlam from '@features/ulam/components/element/mission-list-ulam'
import MissionListUlamAction from '@features/ulam/components/ui/mission-list-ulam-action'
import MissionListUlamTitle from '@features/ulam/components/ui/mission-list-ulam-title'
import MissionListPageHeaderWrapper from '@features/v2/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageSidebarWrapper from '@features/v2/common/components/layout/mission-list-page-sidebar-wrapper'
import MissionListPageWrapper from '@features/v2/common/components/layout/mission-list-page-wrapper'
import { Icon } from '@mtes-mct/monitor-ui'
import React from 'react'

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
