import Text from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'
import { useVessel } from '../../hooks/use-vessel'

type VesselNameProps = {
  name?: string
  flagState?: string
  externalReferenceNumber?: string
}

export const VesselName: React.FC<VesselNameProps> = ({ name, flagState, externalReferenceNumber }) => {
  const { getFullVesselName } = useVessel()

  const text = getFullVesselName(name, flagState, externalReferenceNumber)

  return (
    <Text as="h3" weight="bold" color={THEME.color.gunMetal} data-testid="vessel-name">
      {text}
    </Text>
  )
}

export default VesselName
