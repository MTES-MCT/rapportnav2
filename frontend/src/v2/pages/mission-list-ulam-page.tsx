import { Mission } from '@common/types/mission-types.ts'
import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { endOfMonth, startOfMonth } from 'date-fns'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionListDateRangeNavigator from '../features/common/components/elements/mission-list-daterange-navigator.tsx'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/layout/mission-list-page-sidebar-wrapper'
import MissionListPageTitle from '../features/common/components/layout/mission-list-page-title.tsx'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import { useMissionList } from '../features/common/hooks/use-mission-list.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import { ExportMode, ExportReportType } from '../features/common/types/mission-export-types.ts'
import MissionCreateDialog from '../features/ulam/components/element/mission-create-dialog.tsx'
import MissionListUlam from '../features/ulam/components/element/mission-list/mission-list-ulam.tsx'

const SIDEBAR_ITEMS = [
  {
    url: '',
    key: 'list',
    icon: Icon.MissionAction
  }
]

const MissionListUlamPage: React.FC = () => {
  const today = new Date()
  const { isLoggedIn } = useAuth()
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
  const { exportMissionReport, exportIsLoading } = useMissionReportExport()

  const handleUpdateDateTime = (currentDate: Date) => {
    const newDateRange = {
      startDateTimeUtc: currentDate.toISOString(),
      endDateTimeUtc: endOfMonth(currentDate).toISOString()
    }
    setQueryParams(newDateRange)
  }

  const exportAEM = async (missionsToExport: Mission[]) => {
    await exportMissionReport({
      missionIds: (missionsToExport ?? []).map((m: Mission) => m.id),
      exportMode: ExportMode.COMBINED_MISSIONS_IN_ONE,
      reportType: ExportReportType.AEM
    })
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle userId={isLoggedIn()?.userId} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="list" items={SIDEBAR_ITEMS} />}
      footer={<></>}
    >
      <MissionListPageContentWrapper
        loading={isLoading}
        hasMissions={!!missions?.length}
        title={'Mes rapports'}
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
                  startDateTimeUtc={queryParams.startDateTimeUtc}
                  onUpdateCurrentDate={handleUpdateDateTime}
                  timeframe={'month'}
                />
              </Stack.Item>
            </Stack>
          </>
        }
        list={<MissionListUlam missions={missions?.map(m => getMissionListItem(m))} />}
      />
      <MissionCreateDialog isOpen={isDialogOpen} onClose={handleCloseDialog} />
    </MissionListPageWrapper>
  )
}

export default MissionListUlamPage
