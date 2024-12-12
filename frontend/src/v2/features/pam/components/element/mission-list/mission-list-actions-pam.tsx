import { FC } from 'react'
import { Stack } from 'rsuite'
import { Accent, Checkbox, Icon, IconButton } from '@mtes-mct/monitor-ui'
import { Mission } from '@common/types/mission-types.ts'
import { ExportReportType } from '../../../../common/types/mission-export-types.ts'

interface MissionListActionsPamProps {
  missions?: Mission[]
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
  )
}

export default MissionListActionsPam
