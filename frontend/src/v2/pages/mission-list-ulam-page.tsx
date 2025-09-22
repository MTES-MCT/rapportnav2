import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { ULAM_SIDEBAR_ITEMS } from '@router/routes.tsx'
import { useStore } from '@tanstack/react-store'
import React, { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { Stack } from 'rsuite'
import ItemListDateRangeNavigator from '../features/common/components/elements/item-list-daterange-navigator.tsx'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { useDate } from '../features/common/hooks/use-date.tsx'
import { useMissionList } from '../features/common/hooks/use-mission-list.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import MissionCreateDialog from '../features/ulam/components/element/mission-create-dialog.tsx'
import MissionListUlam from '../features/ulam/components/element/mission-list/mission-list-ulam.tsx'
import { store } from '../store/index.ts'

const MissionListUlamPage: React.FC = () => {
  const { getTodayMonthRange } = useDate()
  const user = useStore(store, state => state.user)
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [searchParams, setSearchParams] = useSearchParams()

  const handleCloseDialog = () => {
    setIsDialogOpen(false)
  }

  useEffect(() => {
    if (searchParams.get('endDateTimeUtc') && searchParams.get('startDateTimeUtc')) return
    setSearchParams(getTodayMonthRange())
    return () => {}
  }, [searchParams, setSearchParams, getTodayMonthRange])

  const { getMissionListItem } = useMissionList()
  const { isLoading, data: missions } = useMissionsQuery(searchParams, 'monthly')

  const handleUpdateDateTime = (currentDate: Date) => {
    setSearchParams(getTodayMonthRange(currentDate))
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="list" items={ULAM_SIDEBAR_ITEMS} />}
      footer={undefined}
    >
      <MissionListPageContentWrapper
        loading={isLoading}
        title={'Mes rapports journaliers'}
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
                <ItemListDateRangeNavigator
                  timeframe={'month'}
                  onUpdateCurrentDate={handleUpdateDateTime}
                  startDateTimeUtc={searchParams.get('startDateTimeUtc')}
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
