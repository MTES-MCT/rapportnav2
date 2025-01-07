import React, { FC } from 'react'
import { Stack } from 'rsuite'
import { Mission } from '@common/types/mission-types.ts'
import ExportFileButton from '../../../../common/components/elements/export-file-button.tsx'

interface MissionListActionsUlamProps {
  onClickExport: (missions: Mission[]) => void
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
