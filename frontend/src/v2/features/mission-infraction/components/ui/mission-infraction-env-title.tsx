import Text from '@common/components/ui/text.tsx'
import { ControlType } from '@common/types/control-types'
import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { useInfraction } from '../../hooks/use-infraction'

interface MissionInfractionEnvTitleProps {
  controlType?: ControlType
  identityControlledPerson?: string
}

const MissionInfractionEnvTitle: React.FC<MissionInfractionEnvTitleProps> = ({
  controlType,
  identityControlledPerson
}) => {
  const { getInfractionByControlTypeTitle } = useInfraction()
  return (
    <Text as="h3" weight="bold" color={THEME.color.gunMetal} data-testid={'env-infraction-control-title'}>
      {!controlType
        ? 'Infraction contrôle de l’environnement'
        : `${identityControlledPerson} | ${getInfractionByControlTypeTitle(controlType)}`}
    </Text>
  )
}

export default MissionInfractionEnvTitle
