import { Loader, Stack } from 'rsuite'

const ActionLoader: React.FC = () => {
  return (
    <Stack
      justifyContent={'center'}
      alignItems={'center'}
      style={{ paddingTop: '5rem' }}
      data-testid={'timeline-loading'}
    >
      <Stack.Item alignSelf={'center'}>
        <Loader center={true} size={'md'} vertical={true} />
      </Stack.Item>
    </Stack>
  )
}

export default ActionLoader
