import { FC } from 'react'
import Text from '@common/components/ui/text'
import { ControlMethod } from '@common/types/control-types'
import { Stack } from 'rsuite'
import { useVessel } from '../../../common/hooks/use-vessel'
import { useControlRegistry } from '../../hooks/use-control-registry.tsx'
import { VesselTypeEnum } from '../../../common/types/vessel-type.ts'

type MissionControlNavSummaryProps = {
  vesselType?: VesselTypeEnum
  controlMethod?: ControlMethod
}

const MissionControlNavSummary: FC<MissionControlNavSummaryProps> = ({ vesselType, controlMethod }) => {
  const { getVesselTypeName } = useVessel()
  const { getControlMethod } = useControlRegistry()
  return (
    <Stack direction={'column'}>
      <Stack.Item style={{ width: '100%' }}>
        <Text as={'h3'}>
          Type de contrôle: <b>{getControlMethod(controlMethod)}</b>
        </Text>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Text as={'h3'}>
          Type de cible: <b>{getVesselTypeName(vesselType)}</b>
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionControlNavSummary
