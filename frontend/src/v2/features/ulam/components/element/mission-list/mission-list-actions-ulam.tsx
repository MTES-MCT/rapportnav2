import { FC } from 'react'
import { Stack } from 'rsuite'
import ExportFileButton from '../../../../common/components/elements/export-file-button.tsx'
import { Mission2 } from '../../../../common/types/mission-types.ts'

interface MissionListActionsUlamProps {
  onClickExport: (missions: Mission2[]) => void
  exportIsLoading: boolean
}

const MissionListActionsUlam: FC<MissionListActionsUlamProps> = ({ onClickExport, exportIsLoading }) => {
  return (
    <Stack justifyContent={'flex-end'} style={{ margin: '1rem 0' }}>
      <Stack.Item>
        <ExportFileButton onClick={onClickExport} isLoading={exportIsLoading}>
          Exporter le tableau AEM du mois
        </ExportFileButton>
      </Stack.Item>
    </Stack>
  )
}

export default MissionListActionsUlam
