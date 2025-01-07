import React, { useState } from 'react'
import {Accent, Button, Icon} from '@mtes-mct/monitor-ui'
import { endOfMonth } from 'date-fns'
import MissionListUlam from '../features/ulam/components/element/mission-list/mission-list-ulam.tsx'
import MissionListPageTitle from '../features/common/components/layout/mission-list-page-title.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/layout/mission-list-page-sidebar-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListDateRangeNavigator from '../features/common/components/elements/mission-list-daterange-navigator.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import { Mission } from '@common/types/mission-types.ts'
import { ExportMode, ExportReportType } from '../features/common/types/mission-export-types.ts'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import MissionListActionsUlam from '../features/ulam/components/element/mission-list/mission-list-actions-ulam.tsx'
import {FlexboxGrid, Stack} from "rsuite";
import MissionCreateDialog from "../features/ulam/components/element/mission-create-dialog.tsx";
import ExportFileButton from "../features/common/components/elements/export-file-button.tsx";

const SIDEBAR_ITEMS = [
  {
    url: '',
    key: 'list',
    icon: Icon.MissionAction
  }
]

const MissionListUlamPage: React.FC = () => {
  const today = new Date()
  const [queryParams, setQueryParams] = useState({
    startDateTimeUtc: today.toISOString(),
    endDateTimeUtc: endOfMonth(today).toISOString()
  })
  const [isDialogOpen, setIsDialogOpen] = useState(false)

  const handleCloseDialog = () => {
    setIsDialogOpen(false)
  }

  const { loading, data: missions } = useMissionsQuery(queryParams)
  const { exportMissionReport, loading: exportIsLoading } = useMissionReportExport()

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
      header={
        <MissionListPageHeaderWrapper //
          title={<MissionListPageTitle />}
        />
      }
      sidebar={
        <MissionListPageSidebarWrapper //
          defaultItemKey="list"
          items={SIDEBAR_ITEMS}
        />
      }
      footer={<></>}
    >
      <MissionListPageContentWrapper
        loading={loading}
        hasMissions={!!missions?.length}
        title={'Mes rapports'}
        // subtitle={'Mes rapports de mission'}
        filters={
        <>
          <Stack direction="column" spacing={'2rem'} style={{width:'100%'}}>
            <Stack.Item  style={{width:'100%'}}>
              <Stack direction="row" justifyContent={"flex-end"} alignItems={"flex-end"}   style={{width:'100%'}}>
                <Stack.Item>
                  <Button Icon={Icon.Plus} accent={Accent.PRIMARY} onClick={() => setIsDialogOpen(true)}>Créer un rapport de mission</Button>
                </Stack.Item>
              </Stack>

            </Stack.Item>
            <Stack.Item style={{width:'100%'}}>
              <MissionListDateRangeNavigator
                startDateTimeUtc={queryParams.startDateTimeUtc}
                onUpdateCurrentDate={handleUpdateDateTime}
                timeframe={'month'}
              />
            </Stack.Item>

          </Stack>

        </>

        }
        // actions={
        //   <MissionListActionsUlam //
        //     onClickExport={() => exportAEM(missions)}
        //     exportIsLoading={exportIsLoading}
        //   />
        // }
        list={
          <MissionListUlam //
            missions={missions}
            onClickExport={exportAEM}
          />
        }
      />
      <MissionCreateDialog isOpen={isDialogOpen} onClose={handleCloseDialog} />

    </MissionListPageWrapper>
  )
}

export default MissionListUlamPage
