import React, { useState } from 'react'
import { Icon } from '@mtes-mct/monitor-ui'
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
        subtitle={'Mes rapports de mission'}
        filters={
          <MissionListDateRangeNavigator
            startDateTimeUtc={queryParams.startDateTimeUtc}
            onUpdateCurrentDate={handleUpdateDateTime}
            timeframe={'month'}
          />
        }
        actions={
          <MissionListActionsUlam //
            onClickExport={() => exportAEM(missions)}
            exportIsLoading={exportIsLoading}
          />
        }
        list={
          <MissionListUlam //
            missions={missions}
            onClickExport={exportAEM}
          />
        }
      />
    </MissionListPageWrapper>
  )
}

export default MissionListUlamPage
