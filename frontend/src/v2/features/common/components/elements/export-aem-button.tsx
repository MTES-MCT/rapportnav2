import { Mission } from '@common/types/mission-types.ts'
import React from 'react'
import { Accent, Button, Icon, THEME } from '@mtes-mct/monitor-ui'
import { useMissionExport } from '../../hooks/use-mission-export.tsx'
import GearIcon from '@rsuite/icons/Gear'
import { useMissionAEMExport } from '../../hooks/use-mission-aem-export.tsx'

interface ExportAEMButtonProps {
  missions: Mission[]
  label?: string
}

const LoadingIcon = () => (
  <GearIcon spin
            width={16}
            height={16}
            color={THEME.color.white}
            style={{ fontSize: '2em', marginRight: '0.5rem' }}
            data-testid={'loading-icon'}
  />
)


const ExportAEMButton: React.FC<ExportAEMButtonProps> = ({missions, label}) => {

  let missionAEMExportInput = {
    ids: []
  }

  missionAEMExportInput.ids = missions.map((mission) => mission.id);

  const {exportMissionAEM, isLoading } = useMissionAEMExport(missionAEMExportInput)

  return (
    <Button accent={Accent.PRIMARY}
            Icon={isLoading ? LoadingIcon : Icon.Download}
            style={{position: 'absolute', right: '20px'}}
            onClick={exportMissionAEM}
            data-testid={'aem-export-btn'}
    >
      {label ? label : 'Exporter'}
    </Button>
  )
}

export default ExportAEMButton
