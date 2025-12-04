import Text from '@common/components/ui/text.tsx'
import { VehicleTypeEnum } from '@common/types/env-mission-types'
import { THEME } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { useVehicule } from '../../../common/hooks/use-vehicule'
import { useVessel } from '../../../common/hooks/use-vessel'
import { Target } from '../../../common/types/target-types'

interface MissionTargetTitleProps {
  target?: Target
  vehicleType?: VehicleTypeEnum
}

const MissionTargetTitle: React.FC<MissionTargetTitleProps> = ({ target, vehicleType }) => {
  const { getVesselTypeName } = useVessel()
  const { getVehiculeType } = useVehicule()
  const [id, setId] = useState<string>()
  const [vehicle, setVehicle] = useState<string>()
  const [vesselType, setVesselType] = useState<string>()

  useEffect(() => {
    setVehicle(getVehiculeType(vehicleType))
    setId(target?.externalData?.registrationNumber ?? target?.identityControlledPerson)
    setVesselType(getVesselTypeName(target?.externalData?.vesselType ?? target?.vesselType))
  }, [target, vehicleType])
  return (
    <Text as="h3" weight="bold" color={THEME.color.gunMetal} data-testid={'target-title'}>
      {`${vehicle ?? ''}${vesselType && vehicle ? ` - ` : ''}${vesselType ?? ''}${vesselType && id ? ` - ` : ''}${id ?? ''}`}
    </Text>
  )
}

export default MissionTargetTitle
