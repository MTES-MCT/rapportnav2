import { ControlType } from '@common/types/control-types'
import React from 'react'
import { Stack } from 'rsuite'
import MissionNatinfTag from '../../../common/components/ui/mission-natinfs-tag'
import { Infraction } from '../../../common/types/target-types'
import { useInfraction } from '../../hooks/use-infraction'
import InfractionSummary from '../layout/infraction-summary'
import MissionInfractionTypeTag from './mission-infraction-type-tag'

interface InfractionNavSummaryProps {
  index: number
  onEdit: () => void
  onDelete: () => void
  controlType?: ControlType
  infraction?: Infraction
}

const InfractionNavSummary: React.FC<InfractionNavSummaryProps> = ({
  index,
  onEdit,
  onDelete,
  infraction,
  controlType
}) => {
  const { getInfractionByControlTypeTitle } = useInfraction()
  return (
    <InfractionSummary
      index={index}
      onEdit={onEdit}
      onDelete={onDelete}
      tags={
        <Stack direction="row" spacing={'0.5rem'} style={{ width: '100%', flexWrap: 'wrap' }}>
          <Stack.Item>
            <MissionInfractionTypeTag type={infraction?.infractionType} />
          </Stack.Item>
          <Stack.Item>
            <MissionNatinfTag natinfs={infraction?.natinfs} />
          </Stack.Item>
        </Stack>
      }
      observations={infraction?.observations ?? 'Aucune observation'}
      title={
        !controlType ? 'Infraction contrôle de l’environnement' : `${getInfractionByControlTypeTitle(controlType)}`
      }
    />
  )
}

export default InfractionNavSummary
