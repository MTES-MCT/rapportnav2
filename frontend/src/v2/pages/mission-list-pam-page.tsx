import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import { useSelector } from '@tanstack/react-store'
import { FC, useState } from 'react'
import { useSearchParams } from 'react-router'
import { Stack } from 'rsuite'
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
import OnlineToggle from '../features/common/components/elements/online-toggle.tsx'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { useMissionList } from '../features/common/hooks/use-mission-list.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import { useOfflineMode } from '../features/common/hooks/use-offline-mode.tsx'
import { useOnlineManager } from '../features/common/hooks/use-online-manager.tsx'
import useMissionsQuery from '../features/common/services/use-missions.tsx'
import { ExportMode, ExportReportType } from '../features/common/types/mission-export-types.ts'
import { MissionListItem } from '../features/common/types/mission-types.ts'
import MissionListActionsPam from '../features/pam/components/element/mission-list/mission-list-actions-pam.tsx'
import MissionListExportDialog from '../features/pam/components/element/mission-list/mission-list-export.tsx'
import MissionListPam from '../features/pam/components/element/mission-list/mission-list-pam.tsx'
import { store } from '../store'

// PAM date-range options: current month, current year, specific period (no default → newest first)
const DATE_MODE_OPTIONS = [DateMode.CURRENT_MONTH, DateMode.CURRENT_YEAR, DateMode.CUSTOM].map(value => ({
  value,
  label: DATE_MODE_LABELS[value]
}))

const MissionListPamPage: FC = () => {
  const isOfflineModeEnabled = useOfflineMode()
  const { isOffline } = useOnlineManager()
  const user = useSelector(store, state => state.user)

  const { getSidebarItems } = useGlobalRoutes()
  const [searchParams, setSearchParams] = useSearchParams()

  const { getMissionListItem } = useMissionList()
  const { isLoading, missions, hasNextPage, isFetchingNextPage, fetchNextPage } = useMissionsQuery(searchParams)

  // Project the raw `MissionListData` payload into the formatted view-model once, then reuse it
  // for the list, the actions bar and the export flow.
  const missionItems: MissionListItem[] = (missions ?? []).map(m => getMissionListItem(m))

  const filtersActive = hasActiveMissionListFilters(searchParams)
  const resetFilters = () => setSearchParams(clearMissionListFilters(searchParams))

  const { exportMissionReport, exportIsLoading } = useMissionReportExport()

  const [selectedMissionIds, setSelectedMissionIds] = useState<number[]>([])

  const [dialogVariant, setDialogVariant] = useState<ExportReportType | undefined>(undefined)
  const [showExportDialog, setShowExportDialog] = useState<boolean>(false)

  function filterBySelectedIndices(
    missions: MissionListItem[] = [],
    selectedIndices: number[] = []
  ): MissionListItem[] {
    return missions.filter((m: MissionListItem) => selectedIndices.indexOf(m.id) !== -1)
  }

  const triggerExport = async (selectedMissions: MissionListItem[], variant: ExportReportType, zip: boolean) => {
    await exportMissionReport({
      missionIds: selectedMissions.map(mission => mission.id),
      exportMode: zip ? ExportMode.MULTIPLE_MISSIONS_ZIPPED : ExportMode.COMBINED_MISSIONS_IN_ONE,
      reportType: variant
    })
    setShowExportDialog(false)
    setSelectedMissionIds([])
    setDialogVariant(undefined)
  }

  const toggleAll = (isChecked?: boolean) => {
    setSelectedMissionIds(!isChecked ? [] : missionItems.map((m: MissionListItem) => m.id))
  }

  const toggleOne = (missionId: number, isChecked?: boolean) => {
    setSelectedMissionIds(
      prevSelected =>
        isChecked
          ? [...prevSelected, missionId] // Add the index if `isChecked` is true
          : prevSelected.filter(id => id !== missionId) // Remove the index if `isChecked` is false
    )
  }

  const toggleDialog = async (variant?: ExportReportType) => {
    const selected = filterBySelectedIndices(missionItems, selectedMissionIds)

    // If only 1 mission → directly export
    if (variant && selected.length === 1) {
      await triggerExport(selected, variant, false) // zip = false for single export
      return
    }

    // Otherwise → open the modal
    setDialogVariant(variant)
    setShowExportDialog(!showExportDialog)
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="missions" items={getSidebarItems()} />}
      footer={
        <Stack style={{ width: '100%', height: '100%' }} justifyContent={'flex-end'} alignItems={'center'}>
          <Stack.Item style={{ marginRight: '4rem' }}>{isOfflineModeEnabled && <OnlineToggle />}</Stack.Item>
        </Stack>
      }
    >
      <MissionListPageContentWrapper
        loading={isLoading}
        hasMissions={!!missions?.length}
        isOffline={isOffline}
        title={'Mes rapports'}
        filters={
          <MissionListFilters
            searchParams={searchParams}
            onChange={setSearchParams}
            dateModeOptions={DATE_MODE_OPTIONS}
            showReportTypeFilter={false}
          />
        }
        actions={
          <MissionListActionsPam
            missions={missionItems}
            selectedMissionIds={selectedMissionIds}
            toggleDialog={toggleDialog}
            toggleAll={toggleAll}
          />
        }
        emptyState={filtersActive ? <MissionListEmptyFiltered onReset={resetFilters} /> : undefined}
        list={
          <MissionListPam //
            missions={missionItems}
            selectedMissionIds={selectedMissionIds}
            toggleOne={toggleOne}
            loadMore={
              <MissionListLoadMore
                hasNextPage={hasNextPage}
                isFetchingNextPage={isFetchingNextPage}
                onLoadMore={fetchNextPage}
              />
            }
          />
        }
      />
      {showExportDialog && (
        <MissionListExportDialog
          availableMissions={filterBySelectedIndices(missionItems, selectedMissionIds)}
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
