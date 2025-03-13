import Text from '@common/components/ui/text.tsx'
import { Icon } from '@mtes-mct/monitor-ui'
import { FC, ReactNode } from 'react'
import { Col, FlexboxGrid, Loader, Stack } from 'rsuite'

interface MissionListContentWrapperProps {
  hasMissions: boolean
  title: string
  subtitle?: string | JSX.Element
  filters?: JSX.Element
  actions?: JSX.Element
  list: ReactNode
  loading: boolean
}

const MissionListPageContentWrapper: FC<MissionListContentWrapperProps> = ({
  title,
  subtitle,
  filters,
  actions,
  list,
  loading,
  hasMissions
}) => {
  return (
    <Stack direction={'column'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack
          direction={'row'}
          spacing={'0.5rem'}
          alignItems={'center'}
          style={{ padding: '0 11.5rem', marginTop: '3rem' }}
        >
          <Stack.Item alignSelf={'baseline'}>
            <Icon.MissionAction size={32} style={{ marginTop: '8px' }} />
          </Stack.Item>
          <Stack.Item>
            <Text as={'h1'} style={{ fontSize: '32px' }}>
              {title}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      {subtitle && (
        <Stack.Item style={{ width: '100%', padding: '0 11.5rem', marginTop: '4rem' }}>
          <Text as={'h1'}>{subtitle}</Text>
        </Stack.Item>
      )}
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="flex-start" spacing="0.2rem" style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%', height: '100%' }}>
            <FlexboxGrid justify="center" style={{ padding: '2rem 2rem 4rem 2rem', display: 'flex', flex: 1 }}>
              <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
                {filters}
              </FlexboxGrid.Item>
              <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
                {loading ? (
                  <div style={{ marginTop: '25rem' }}>
                    <Loader
                      center={true}
                      size={'md'}
                      vertical={true}
                      content={<Text as={'h3'}>Missions en cours de chargement</Text>}
                    />
                  </div>
                ) : !hasMissions ? (
                  <div style={{ marginTop: '10rem' }}>
                    <Text as={'h3'} style={{ textAlign: 'center' }}>
                      Aucune mission pour cette période de temps.
                    </Text>
                  </div>
                ) : (
                  <>
                    {actions}

                    {list}
                  </>
                )}
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionListPageContentWrapper
