import Text from '@common/components/ui/text.tsx'
import { Banner, Icon, Level, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'

export type ActionHeaderSyncStatusBannerProps = {}

const ActionHeaderSyncStatusBanner: FC<ActionHeaderSyncStatusBannerProps> = () => {
  return (
    <div data-testid={'report-status-banner'}>
      <Banner isClosable={true} isCollapsible={false} isHiddenByDefault={false} level={Level.ERROR} top={'0'}>
        <Stack direction={'row'} alignItems={'center'} justifyContent={'flex-start'} spacing={'0.5rem'}>
          <Stack.Item style={{ paddingTop: '8px' }}>
            <Icon.Offline color={THEME.color.maximumRed} />
          </Stack.Item>
          <Stack.Item>
            <Text as={'h3'} weight={'bold'} color={THEME.color.maximumRed}>
              Attention, cette action n'est pas encore synchronisée avec le serveur
            </Text>
          </Stack.Item>
        </Stack>
      </Banner>
    </div>
  )
}

export default ActionHeaderSyncStatusBanner
