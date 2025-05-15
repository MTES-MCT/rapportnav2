import { Accent, Checkbox, Icon, IconButton } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { ExportReportType } from '../../../../common/types/mission-export-types.ts'
import { Mission2 } from '../../../../common/types/mission-types.ts'
import { useOnlineManager } from '../../../../common/hooks/use-online-manager.tsx'

interface MissionListActionsPamProps {
  missions?: Mission2[]
  selectedMissionIds: number[]
  toggleDialog: (variant?: ExportReportType) => void
  toggleAll: (isChecked?: boolean) => void
}

const MissionListActionsPam: FC<MissionListActionsPamProps> = ({
  missions,
  selectedMissionIds,
  toggleDialog,
  toggleAll
}) => {
  const { isOffline } = useOnlineManager()
  return (
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
          disabled={!selectedMissionIds.length || isOffline}
          title={isOffline ? "Cette action n'est pas disponible hors ligne" : 'Export tableau AEM'}
        />
      </Stack.Item>
      <Stack.Item>
        <IconButton
          Icon={Icon.MissionAction}
          accent={Accent.SECONDARY}
          onClick={() => toggleDialog(ExportReportType.PATROL)}
          disabled={!selectedMissionIds.length || isOffline}
          title={isOffline ? "Cette action n'est pas disponible hors ligne" : 'Export rapport de patrouille'}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionListActionsPam
