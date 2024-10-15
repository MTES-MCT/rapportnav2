import { THEME } from '@mtes-mct/monitor-ui'
import Text from './text.tsx'

export const TextByCacem: React.FC = () => {
  return (
    <Text as="h4" color={THEME.color.mediumSeaGreen} fontStyle={'italic'}>
      ajouté par CACEM
    </Text>
  )
}

export default TextByCacem
