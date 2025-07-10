import { Mission } from '@common/types/mission-types.ts'
import { Icon } from '@mtes-mct/monitor-ui'
import { FC, useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { Stack } from 'rsuite'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionListDateRangeNavigator from '../features/common/components/elements/mission-list-daterange-navigator.tsx'
import OnlineToggle from '../features/common/components/elements/online-toggle.tsx'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { useDate } from '../features/common/hooks/use-date.tsx'
import { useMissionList } from '../features/common/hooks/use-mission-list.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import { ExportMode, ExportReportType } from '../features/common/types/mission-export-types.ts'
import { Mission2 } from '../features/common/types/mission-types.ts'
import MissionListActionsPam from '../features/pam/components/element/mission-list/mission-list-actions-pam.tsx'
import MissionListExportDialog from '../features/pam/components/element/mission-list/mission-list-export.tsx'
import MissionListPam from '../features/pam/components/element/mission-list/mission-list-pam.tsx'

const SIDEBAR_ITEMS = [
  {
    url: '',
    key: 'list',
    icon: Icon.MissionAction
  }
]

const MissionListPamPage: FC = () => {
  const { isLoggedIn } = useAuth()
  const { getTodayYearRange } = useDate()
  const [searchParams, setSearchParams] = useSearchParams()

  useEffect(() => {
    if (searchParams.get('endDateTimeUtc') && searchParams.get('startDateTimeUtc')) return
    setSearchParams(getTodayYearRange())
    return () => {}
  }, [searchParams, setSearchParams, getTodayYearRange])

  const { getMissionListItem } = useMissionList()
  const { isLoading, data: missions } = useMissionsQuery(searchParams)

  const { exportMissionReport, exportIsLoading } = useMissionReportExport()

  const [selectedMissionIds, setSelectedMissionIds] = useState<number[]>([])

  const [dialogVariant, setDialogVariant] = useState<ExportReportType | undefined>(undefined)
  const [showExportDialog, setShowExportDialog] = useState<boolean>(false)

  function filterBySelectedIndices(missions: Mission2[] = [], selectedIndices: number[] = []): Mission2[] {
    return missions.filter((m: Mission2) => selectedIndices.indexOf(m.id) !== -1)
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
    setSelectedMissionIds(!isChecked ? [] : (missions ?? []).map((m: Mission2) => m.id))
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
    setSearchParams(getTodayYearRange(currentDate))
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle userId={isLoggedIn()?.userId} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="list" items={SIDEBAR_ITEMS} />}
      footer={
        <Stack style={{ width: '100%', height: '100%' }} justifyContent={'flex-end'} alignItems={'center'}>
          <Stack.Item style={{ marginRight: '4rem' }}>
            <OnlineToggle />
          </Stack.Item>
        </Stack>
      }
    >
      <MissionListPageContentWrapper
        loading={isLoading}
        hasMissions={!!missions?.length}
        title={'Mes rapports'}
        filters={
          <MissionListDateRangeNavigator
            startDateTimeUtc={searchParams.get('startDateTimeUtc')}
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
          <MissionListPam
            missions={missions?.map(m => getMissionListItem(m))}
            selectedMissionIds={selectedMissionIds}
            toggleOne={toggleOne}
          />
        }
      />
      {showExportDialog && (
        <MissionListExportDialog
          availableMissions={filterBySelectedIndices(missions, selectedMissionIds)?.map(m => getMissionListItem(m))}
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
