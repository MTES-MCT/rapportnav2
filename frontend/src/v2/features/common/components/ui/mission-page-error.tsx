import Text from '@common/components/ui/text'
import { Accent, Button, Size } from '@mtes-mct/monitor-ui'
import { ROOT_PATH } from '@router/routes'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'

type MissionPageErrorProps = {
  error?: { message?: string }
}

const MissionPageError: React.FC<MissionPageErrorProps> = ({ error }: MissionPageErrorProps) => {
  const navigate = useNavigate()
  return (
    <div
      style={{
        margin: 0,
        marginTop: '10rem',
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        maxHeight: '100vh'
      }}
    >
      <Stack justifyContent={'center'} direction={'column'} spacing={'2rem'}>
        <Stack.Item style={{ maxWidth: '33%' }}>
          <Text as={'h2'}>Une erreur est survenue</Text>
          <Text as={'h3'}>{error?.message}</Text>
        </Stack.Item>
        <Stack.Item>
          <Button accent={Accent.PRIMARY} size={Size.LARGE} onClick={() => navigate(ROOT_PATH)}>
            Retourner à l'accueil
          </Button>
        </Stack.Item>
      </Stack>
    </div>
  )
}

export default MissionPageError
