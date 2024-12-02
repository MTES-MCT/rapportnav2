import React, { JSX, useState } from 'react'
import { Col, Divider, FlexboxGrid, Loader, Stack } from 'rsuite'
import { Accent, Checkbox, Icon, IconButton, Tag, THEME } from '@mtes-mct/monitor-ui'
import { Mission } from '@common/types/mission-types.ts'
import MissionListHeaderPam from './mission-list-header-pam.tsx'
import MissionListItemPam from './mission-list-item-pam.tsx'
import MissionListExportDialog from './mission-list-export.tsx'
import Text from '@common/components/ui/text.tsx'
import { useMissionReportExport } from '../../../common/hooks/use-mission-report-export.tsx'
import { ExportMode, ExportReportType } from '../../../common/types/mission-export-types.ts'
import { getMonthName } from '@common/utils/dates-for-humans.ts'

interface MissionListPamProps {
  missions?: Mission[]
  loading: boolean
  dateRangeNavigator: JSX.Element
}

const groupMissionsByMonth = (missions: Mission[]) => {
  const grouped: Record<string, Mission[]> = {}

  missions.forEach(mission => {
    const startDate = new Date(mission.startDateTimeUtc)
    const monthKey = `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(2, '0')}`
    if (!grouped[monthKey]) {
      grouped[monthKey] = []
    }
    grouped[monthKey].push(mission)
  })

  // Sort by monthKey in descending order and return as array of [key, missions]
  return Object.entries(grouped).sort(([a], [b]) => b.localeCompare(a))
}

const MissionListPam: React.FC<MissionListPamProps> = ({ missions, loading, dateRangeNavigator }) => {
  const { exportMissionReport, loading: exportIsLoading } = useMissionReportExport()

  const [selectedMissionIds, setSelectedMissionIds] = useState<number[]>([])

  const [dialogVariant, setDialogVariant] = useState<ExportReportType | undefined>(undefined)
  const [showExportDialog, setShowExportDialog] = useState<boolean>(false)

  function filterBySelectedIndices(missions: Mission[] = [], selectedIndices: number[]): Mission[] {
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

  return (
    <>
      <Stack direction="column" alignItems="flex-start" spacing="0.2rem" style={{ width: '100%' }}>
        <Stack.Item style={{ width: '100%', height: '100%' }}>
          <FlexboxGrid justify="center" style={{ padding: '4rem 2rem', display: 'flex', flex: 1 }}>
            <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
              {dateRangeNavigator}
            </FlexboxGrid.Item>
            <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
              {loading ? (
                <div style={{ marginTop: '25rem' }}>
                  <Loader
                    center={true}
                    size={'md'}
                    vertical={true}
                    content={<Text as={'h3'}>Missions en cours de chargement</Text>}
                  />
                </div>
              ) : missions?.length === 0 ? (
                <div style={{ marginTop: '10rem' }}>
                  <Text as={'h3'} style={{ textAlign: 'center' }}>
                    Aucune mission pour cette période de temps.
                  </Text>
                </div>
              ) : (
                <>
                  <Stack direction={'row'} spacing={'0.2rem'} style={{ padding: '0 1rem' }}>
                    <Stack.Item>
                      <Checkbox
                        label={''}
                        checked={selectedMissionIds.length === missions?.length}
                        title={'Tout sélectionner'}
                        style={{ margin: '0 16px 16px 16px' }}
                        onChange={toggleAll}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ marginLeft: '8px' }}>
                      <IconButton
                        Icon={Icon.ListLines}
                        accent={Accent.SECONDARY}
                        onClick={() => toggleDialog(ExportReportType.AEM)}
                        title={'Export tableau AEM'}
                        disabled={!selectedMissionIds.length}
                      />
                    </Stack.Item>
                    <Stack.Item>
                      <IconButton
                        Icon={Icon.MissionAction}
                        accent={Accent.SECONDARY}
                        onClick={() => toggleDialog(ExportReportType.PATROL)}
                        title={'Export rapport de patrouille'}
                        disabled={!selectedMissionIds.length}
                      />
                    </Stack.Item>
                  </Stack>
                  <Stack direction={'column'} spacing={'0.2rem'} style={{ width: '100%' }}>
                    <Stack.Item style={{ width: '100%' }}>
                      <MissionListHeaderPam />
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <Stack
                        direction={'column'}
                        spacing={'0.2rem'}
                        style={{ width: '100%', overflowY: 'auto', height: '50vh', minHeight: '50vh' }}
                      >
                        {groupMissionsByMonth(missions).map(([monthKey, missions]) => (
                          <>
                            {/* Render Month Name */}
                            <Stack.Item style={{ width: '100%' }} key={monthKey}>
                              <Stack direction={'row'}>
                                <Stack.Item>
                                  <Tag accent={Accent.PRIMARY} style={{ minWidth: '50px' }}>
                                    <Text as={'h3'} weight={'bold'}>
                                      {getMonthName(monthKey)}
                                    </Text>
                                  </Tag>
                                </Stack.Item>
                                <Stack.Item style={{ width: '100%' }}>
                                  <Divider style={{ backgroundColor: THEME.color.charcoal, marginTop: '28px' }} />
                                </Stack.Item>
                              </Stack>
                            </Stack.Item>

                            {/* Render Missions for the Month */}
                            {missions.map(mission => (
                              <Stack.Item key={mission.id} style={{ width: '100%' }}>
                                <MissionListItemPam
                                  mission={mission}
                                  isSelected={selectedMissionIds.indexOf(mission.id) !== -1}
                                  onToggle={toggleOne}
                                />
                              </Stack.Item>
                            ))}
                          </>
                        ))}
                      </Stack>
                    </Stack.Item>
                  </Stack>
                </>
              )}
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </Stack.Item>
      </Stack>
      {showExportDialog && (
        <MissionListExportDialog
          availableMissions={filterBySelectedIndices(missions, selectedMissionIds)}
          toggleDialog={toggleDialog}
          triggerExport={triggerExport}
          exportInProgress={exportIsLoading}
          variant={dialogVariant}
        />
      )}
    </>
  )
}

export default MissionListPam
