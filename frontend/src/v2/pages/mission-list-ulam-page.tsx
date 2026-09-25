import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import { useStore } from '@tanstack/react-store'
import React, { useState } from 'react'
import { useSearchParams } from 'react-router'
import { Stack } from 'rsuite'
import MissionListCountTag from '../features/common/components/elements/mission-list-count-tag.tsx'
import MissionListEmptyFiltered from '../features/common/components/elements/mission-list-empty-filtered.tsx'
import {
  clearMissionListFilters,
  hasActiveMissionListFilters
} from '../features/common/components/elements/mission-list-filter-utils.ts'
import MissionListFilters, {
  DateMode,
  DATE_MODE_LABELS
} from '../features/common/components/elements/mission-list-filters.tsx'
import MissionListLoadMore from '../features/common/components/elements/mission-list-load-more.tsx'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { useMissionList } from '../features/common/hooks/use-mission-list.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import MissionCreateDialog from '../features/ulam/components/element/mission-create-dialog.tsx'
import MissionListUlam from '../features/ulam/components/element/mission-list/mission-list-ulam.tsx'
import { store } from '../store'

// ULAM date-range options: current week, current month, specific period (no default → newest first)
const DATE_MODE_OPTIONS = [DateMode.CURRENT_WEEK, DateMode.CURRENT_MONTH, DateMode.CUSTOM].map(value => ({
  value,
  label: DATE_MODE_LABELS[value]
}))

const MissionListUlamPage: React.FC = () => {
  const { getSidebarItems } = useGlobalRoutes()
  const user = useStore(store, state => state.user)
  const [isDialogOpen, setIsDialogOpen] = useState(false)
  const [searchParams, setSearchParams] = useSearchParams()

  const handleCloseDialog = () => {
    setIsDialogOpen(false)
  }

  const { getMissionListItem } = useMissionList()
  const { isLoading, missions, hasNextPage, isFetchingNextPage, fetchNextPage } = useMissionsQuery(searchParams)

  const filtersActive = hasActiveMissionListFilters(searchParams)
  const resetFilters = () => setSearchParams(clearMissionListFilters(searchParams))

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="missions" items={getSidebarItems()} />}
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
                <MissionListFilters
                  searchParams={searchParams}
                  onChange={setSearchParams}
                  dateModeOptions={DATE_MODE_OPTIONS}
                />
              </Stack.Item>
            </Stack>
          </>
        }
        emptyState={filtersActive ? <MissionListEmptyFiltered onReset={resetFilters} /> : undefined}
        list={
          <Stack direction="column" spacing="1rem" style={{ width: '100%' }}>
            <Stack.Item alignSelf={'flex-end'} style={{ width: '100%' }}>
              <MissionListCountTag count={missions.length} />
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <MissionListUlam
                missions={missions?.map(m => getMissionListItem(m))}
                user={user}
                loadMore={
                  <MissionListLoadMore
                    hasNextPage={hasNextPage}
                    isFetchingNextPage={isFetchingNextPage}
                    onLoadMore={fetchNextPage}
                  />
                }
              />
            </Stack.Item>
          </Stack>
        }
      />
      <MissionCreateDialog isOpen={isDialogOpen} onClose={handleCloseDialog} />
    </MissionListPageWrapper>
  )
}

export default MissionListUlamPage
