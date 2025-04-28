import { ControlType } from '@common/types/control-types.ts'
import React from 'react'
import MissionNatinfTag from '../../../common/components/ui/mission-natinfs-tag.tsx'
import { Infraction } from '../../../common/types/target-types.ts'
import MissionInfractionSummary from '../layout/mission-infraction-summary.tsx'

interface MissionInfractionSummaryProps {
  controlType?: ControlType
  infraction: Infraction
  onEdit?: (id?: string) => void
  onDelete?: (id?: string) => void
}

const MissionInfractionNavSummary: React.FC<MissionInfractionSummaryProps> = ({
  onEdit,
  onDelete,
  infraction,
  controlType
}) => {
  return (
    <MissionInfractionSummary
      onEdit={onEdit}
      onDelete={onDelete}
      controlType={controlType}
      infractions={[infraction]}
      summary={({ infraction }) => <MissionNatinfTag natinfs={(infraction as Infraction).natinfs} />}
    />
  )
}

export default MissionInfractionNavSummary
