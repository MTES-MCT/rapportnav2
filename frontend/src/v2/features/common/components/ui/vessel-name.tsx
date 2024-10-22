import Text from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'
import { useVessel } from '../../hooks/use-vessel'

type VesselNameProps = {
  name?: string
}

export const VesselName: React.FC<VesselNameProps> = ({ name }) => {
  const { getVesselName } = useVessel()
  return (
    <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
      {getVesselName(name)}
    </Text>
  )
}

export default VesselName
