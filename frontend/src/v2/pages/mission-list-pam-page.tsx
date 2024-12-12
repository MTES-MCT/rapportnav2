import React, { FC, useState } from 'react'
import { Icon } from '@mtes-mct/monitor-ui'
import MissionListPageTitle from '../features/common/components/layout/mission-list-page-title.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/layout/mission-list-page-sidebar-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListDateRangeNavigator from '../features/common/components/elements/mission-list-daterange-navigator.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import MissionListPam from '../features/pam/components/element/mission-list/mission-list-pam.tsx'
import { endOfYear } from 'date-fns/endOfYear'
import { startOfYear } from 'date-fns/startOfYear'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import { ExportMode, ExportReportType } from '../features/common/types/mission-export-types.ts'
import { Mission } from '@common/types/mission-types.ts'
import MissionListExportDialog from '../features/pam/components/element/mission-list/mission-list-export.tsx'
import MissionListActionsPam from '../features/pam/components/element/mission-list/mission-list-actions-pam.tsx'

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

  const { loading, data: missions } = useMissionsQuery(queryParams)

  const { exportMissionReport, loading: exportIsLoading } = useMissionReportExport()

  const [selectedMissionIds, setSelectedMissionIds] = useState<number[]>([])

  const [dialogVariant, setDialogVariant] = useState<ExportReportType | undefined>(undefined)
  const [showExportDialog, setShowExportDialog] = useState<boolean>(false)

  function filterBySelectedIndices(missions: Mission[] = [], selectedIndices: number[] = []): Mission[] {
    return missions.filter((m: Mission) => selectedIndices.indexOf(m.id) !== -1)
  }

  const triggerExport = async (missions: Mission[], variant: ExportReportType, zip: boolean) => {
    await exportMissionReport({
      missionIds: missions.map(mission => mission.id),
      exportMode: zip ? ExportMode.MULTIPLE_MISSIONS_ZIPPED : ExportMode.COMBINED_MISSIONS_IN_ONE,
      reportType: variant
    })
    setShowExportDialog(false)
    setSelectedMissionIds([])
    setDialogVariant(undefined)
  }

  const toggleAll = (isChecked?: boolean) => {
    setSelectedMissionIds(!isChecked ? [] : (missions ?? []).map((m: Mission) => m.id))
  }

  const toggleOne = (missionId: number, isChecked?: boolean) => {
    setSelectedMissionIds(
      prevSelected =>
        isChecked
          ? [...prevSelected, missionId] // Add the index if `isChecked` is true
          : prevSelected.filter(id => id !== missionId) // Remove the index if `isChecked` is false
    )
  }

  const toggleDialog = (variant?: ExportReportType) => {
    setDialogVariant(variant)
    setShowExportDialog(!showExportDialog)
  }

  const handleUpdateDateTime = (currentDate: Date) => {
    const newDateRange = {
      startDateTimeUtc: currentDate.toISOString(),
      endDateTimeUtc: endOfYear(currentDate).toISOString()
    }
    setQueryParams(newDateRange)
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="list" items={SIDEBAR_ITEMS} />}
      footer={<></>}
    >
      <MissionListPageContentWrapper
        loading={loading}
        hasMissions={!!missions?.length}
        title={'Mes rapports'}
        // subtitle={} // unnecessary for PAM version
        filters={
          <MissionListDateRangeNavigator
            startDateTimeUtc={queryParams.startDateTimeUtc}
            onUpdateCurrentDate={handleUpdateDateTime}
            timeframe={'year'}
          />
        }
        actions={
          <MissionListActionsPam
            missions={missions}
            selectedMissionIds={selectedMissionIds}
            toggleDialog={toggleDialog}
            toggleAll={toggleAll}
          />
        }
        list={
          <MissionListPam //
            missions={missions}
            selectedMissionIds={selectedMissionIds}
            toggleOne={toggleOne}
          />
        }
      />
      {showExportDialog && (
        <MissionListExportDialog
          availableMissions={filterBySelectedIndices(missions, selectedMissionIds)}
          toggleDialog={toggleDialog}
          triggerExport={triggerExport}
          exportInProgress={exportIsLoading}
          variant={dialogVariant}
        />
      )}
    </MissionListPageWrapper>
  )
}

export default MissionListPamPage
