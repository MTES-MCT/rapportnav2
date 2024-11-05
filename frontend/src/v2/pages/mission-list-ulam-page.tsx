import React, { useState } from 'react'
import { Icon } from '@mtes-mct/monitor-ui'
import { Loader } from 'rsuite'
import { endOfMonth } from 'date-fns'
import MissionListUlam from '../../v2/features/ulam/components/element/mission-list-ulam'
import MissionListUlamAction from '../../v2/features/ulam/components/ui/mission-list-ulam-action'
import MissionListUlamTitle from '../../v2/features/ulam/components/ui/mission-list-ulam-title'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/layout/mission-list-page-sidebar-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListDateRangeNavigator from '../features/common/components/elements/mission-list-daterange-navigator.tsx'
import MissionListing from '../features/common/components/elements/mission-listing.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'

const SIDEBAR_ITEMS = [
  {
    url: '',
    key: 'list',
    icon: Icon.MissionAction,
  },
]

const MissionListUlamPage: React.FC = () => {
  const today = new Date()
  const [queryParams, setQueryParams] = useState({
    startDateTimeUtc: today.toISOString(),
    endDateTimeUtc: endOfMonth(today).toISOString(),
  })

  let { loading, data } = useMissionsQuery(queryParams)

  const handleUpdateDateTime = (currentDate: Date) => {

    const newDateRange = {
      startDateTimeUtc: currentDate.toISOString(),
      endDateTimeUtc: endOfMonth(currentDate).toISOString()
    }
    setQueryParams(newDateRange)
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListUlamTitle />} actions={<MissionListUlamAction />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="list" items={SIDEBAR_ITEMS} />}
      footer={<></>}
    >
      {loading ? (
        <Loader content="Chargement des missions..." vertical={false} />
      ) : (
        <MissionListUlam
          dateRangeNavigator={
            <MissionListDateRangeNavigator
              startDateTimeUtc={queryParams.startDateTimeUtc}
              onUpdateCurrentDate={handleUpdateDateTime}
            />
          }
          missionListing={<MissionListing isUlam missions={data} />}
        />
      )}
    </MissionListPageWrapper>
  )
}

export default MissionListUlamPage
