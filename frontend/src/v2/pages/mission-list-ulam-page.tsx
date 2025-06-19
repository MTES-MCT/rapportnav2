import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { useStore } from '@tanstack/react-store'
import { endOfMonth, startOfMonth } from 'date-fns'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import MissionListDateRangeNavigator from '../features/common/components/elements/mission-list-daterange-navigator.tsx'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { useMissionList } from '../features/common/hooks/use-mission-list.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import MissionCreateDialog from '../features/ulam/components/element/mission-create-dialog.tsx'
import MissionListUlam from '../features/ulam/components/element/mission-list/mission-list-ulam.tsx'
import { store } from '../store/index.ts'
import { UTCDate } from '@date-fns/utc'

const SIDEBAR_ITEMS = [
  {
    url: '',
    key: 'list',
    icon: Icon.MissionAction
  }
]

const MissionListUlamPage: React.FC = () => {
  const today = new UTCDate()
  const user = useStore(store, state => state.user)
  const [queryParams, setQueryParams] = useState({
    startDateTimeUtc: startOfMonth(today).toISOString(),
    endDateTimeUtc: endOfMonth(today).toISOString()
  })
  const [isDialogOpen, setIsDialogOpen] = useState(false)

  const handleCloseDialog = () => {
    setIsDialogOpen(false)
  }

  const { getMissionListItem } = useMissionList()
  const { isLoading, data: missions } = useMissionsQuery(queryParams)

  const handleUpdateDateTime = (currentDate: Date) => {
    const newDateRange = {
      startDateTimeUtc: currentDate.toISOString(),
      endDateTimeUtc: endOfMonth(currentDate).toISOString()
    }
    setQueryParams(newDateRange)
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="list" items={SIDEBAR_ITEMS} />}
      footer={undefined}
    >
      <MissionListPageContentWrapper
        loading={isLoading}
        title={'Mes rapports'}
        hasMissions={!!missions?.length}
        filters={
          <>
            <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" justifyContent={'flex-end'} alignItems={'flex-end'} style={{ width: '100%' }}>
                  <Stack.Item>
                    <Button Icon={Icon.Plus} accent={Accent.PRIMARY} onClick={() => setIsDialogOpen(true)}>
                      Créer un rapport de mission
                    </Button>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <MissionListDateRangeNavigator
                  timeframe={'month'}
                  onUpdateCurrentDate={handleUpdateDateTime}
                  startDateTimeUtc={queryParams.startDateTimeUtc}
                />
              </Stack.Item>
            </Stack>
          </>
        }
        list={<MissionListUlam missions={missions?.map(m => getMissionListItem(m))} user={user} />}
      />
      <MissionCreateDialog isOpen={isDialogOpen} onClose={handleCloseDialog} />
    </MissionListPageWrapper>
  )
}

export default MissionListUlamPage
