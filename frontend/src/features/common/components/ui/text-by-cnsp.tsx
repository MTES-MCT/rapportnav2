import { THEME } from '@mtes-mct/monitor-ui'
import Text from './text.tsx'

export const TextByCnsp: React.FC = () => {
  return (
    <Text as="h4" color={THEME.color.blueGray} fontStyle="italic">
      ajouté par CNSP
    </Text>
  )
}

export default TextByCnsp
