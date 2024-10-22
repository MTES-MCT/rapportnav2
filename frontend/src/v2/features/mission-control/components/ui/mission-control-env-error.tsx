import Text from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'

type MissionControlEnvErrorProps = {}

const MissionControlEnvError: React.FC<MissionControlEnvErrorProps> = () => {
  return (
    <div style={{ width: '100%', marginTop: '5px' }}>
      <Text as={'h4'} color={THEME.color.maximumRed} fontStyle={'italic'}>
        Attention, le chiffre renseigné ne peut pas être supérieur au nombre total de contrôles effectués
      </Text>
    </div>
  )
}

export default MissionControlEnvError
