import Text from '@common/components/ui/text'
import { THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'

type MissionActionErrorProps = {
  error: { message: string } & unknown
}
const MissionActionError: React.FC<MissionActionErrorProps> = ({ error }) => {
  return (
    <Stack justifyContent={'center'} alignItems={'center'} style={{ height: '100%' }} data-testid={'timeline-error'}>
      <Stack.Item alignSelf={'center'}>
        <Text as={'h3'} color={THEME.color.maximumRed}>
          Une erreur s'est produite lors du chargement de la timeline.
          <br />
          Si le problème persiste, veuillez contacter l'équipe RapportNav.
        </Text>
        <Text as={'h3'} color={THEME.color.lightGray}>
          Erreur: {error.message}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionError
