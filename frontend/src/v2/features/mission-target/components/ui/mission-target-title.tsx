import Text from '@common/components/ui/text.tsx'
import { VehicleTypeEnum } from '@common/types/env-mission-types'
import { Accent, Tag, THEME } from '@mtes-mct/monitor-ui'
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
  const [title, setTitle] = useState<string>()
  const [nbTarget, setNbTarget] = useState<number>(1)

  const getTitleGroup = (target?: Target) => {
    const vesselType = target?.externalData?.vesselType ?? target?.vesselType
    return vesselType ? 'type de véhicule en infraction' : 'personnes physiques/morales en infraction'
  }

  const getTitleSingle = (target?: Target, vehicleType?: VehicleTypeEnum) => {
    const vehicle = getVehiculeType(vehicleType)
    const id = target?.externalData?.registrationNumber ?? target?.identityControlledPerson
    const vesselType = getVesselTypeName(target?.externalData?.vesselType ?? target?.vesselType)
    return `${vehicle ?? ''}${vesselType && vehicle ? ` - ` : ''}${vesselType ?? ''}${vesselType && id ? ` - ` : ''}${id ?? ''}`
  }

  const getTitle = (target?: Target, vehicleType?: VehicleTypeEnum): string => {
    return (target?.externalData?.nbTarget ?? 0) > 1 ? getTitleGroup(target) : getTitleSingle(target, vehicleType)
  }

  useEffect(() => {
    setTitle(getTitle(target, vehicleType))
    setNbTarget(target?.externalData?.nbTarget ?? 1)
  }, [target, vehicleType])

  return (
    <div style={{ width: '100%', display: 'flex', flexDirection: 'row' }}>
      {nbTarget > 1 && (
        <Tag accent={Accent.PRIMARY} data-testid={'target-tag'} style={{ marginRight: 6, paddingTop: 2 }}>
          <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
            {nbTarget}
          </Text>
        </Tag>
      )}
      <Text as="h3" weight="bold" color={THEME.color.gunMetal} data-testid={'target-title'}>
        {title}
      </Text>
    </div>
  )
}

export default MissionTargetTitle
