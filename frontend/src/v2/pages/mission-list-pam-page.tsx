import React, { FC, useState } from 'react'
import { Icon } from '@mtes-mct/monitor-ui'
import { Loader, Stack } from 'rsuite'
import { endOfMonth } from 'date-fns'
import MissionListUlam from '../../v2/features/ulam/components/element/mission-list-ulam'

import MissionListUlamTitle from '../../v2/features/ulam/components/ui/mission-list-ulam-title'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/layout/mission-list-page-sidebar-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListDateRangeNavigator from '../features/common/components/elements/mission-list-daterange-navigator.tsx'
import MissionListing from '../features/common/components/elements/mission-listing.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import ExportFileButton from '../features/common/components/elements/export-file-button.tsx'
import Text from '@common/components/ui/text.tsx'
import MissionListPam from '../features/pam/components/element/mission-list-pam.tsx'
import { endOfYear } from 'date-fns/endOfYear'
import { startOfYear } from 'date-fns/startOfYear'

const SIDEBAR_ITEMS = [
  {
    url: '',
    key: 'list',
    icon: Icon.MissionAction
  }
]

const MissionListPamPage: FC = () => {
  const today = new Date()
  const [queryParams, setQueryParams] = useState({
    startDateTimeUtc: startOfYear(today.toISOString()),
    endDateTimeUtc: endOfYear(today).toISOString()
  })

  const { loading, data } = useMissionsQuery(queryParams)

  const handleUpdateDateTime = (currentDate: Date) => {
    const newDateRange = {
      startDateTimeUtc: currentDate.toISOString(),
      endDateTimeUtc: endOfYear(currentDate).toISOString()
    }
    setQueryParams(newDateRange)
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListUlamTitle />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="list" items={SIDEBAR_ITEMS} />}
      footer={<></>}
    >
      <Stack direction={'column'} style={{ width: '100%' }}>
        <Stack.Item style={{ width: '100%' }}>
          <Stack
            direction={'row'}
            spacing={'0.5rem'}
            alignItems={'center'}
            style={{ padding: '0 15rem', marginTop: '5rem' }}
          >
            <Stack.Item alignSelf={'baseline'}>
              <Icon.MissionAction size={32} style={{ marginTop: '8px' }} />
            </Stack.Item>
            <Stack.Item>
              <Text as={'h1'} style={{ fontSize: '32px' }}>
                Mes rapports
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MissionListPam
            loading={loading}
            missions={data}
            dateRangeNavigator={
              <MissionListDateRangeNavigator
                startDateTimeUtc={queryParams.startDateTimeUtc}
                onUpdateCurrentDate={handleUpdateDateTime}
                timeframe={'year'}
              />
            }
          />
        </Stack.Item>
      </Stack>
    </MissionListPageWrapper>
  )
}

export default MissionListPamPage
