import Text from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'

export const TextByCacem: React.FC = () => {
  return (
    <Text as="h4" color={THEME.color.mediumSeaGreen} fontStyle={'italic'}>
      ajouté par CACEM
    </Text>
  )
}

export default TextByCacem
