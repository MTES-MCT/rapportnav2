import Text from '@common/components/ui/text.tsx'
import { FC, JSX, ReactNode } from 'react'
import { Col, FlexboxGrid, Loader, Stack } from 'rsuite'

interface MissionListContentWrapperProps {
  hasMissions: boolean
  title: string
  icon?: JSX.Element
  subtitle?: string | JSX.Element
  filters?: JSX.Element
  actions?: JSX.Element
  list: ReactNode
  loading: boolean
  isOffline?: boolean
  // optional custom empty state rendered in place of the default "no mission" text (e.g. the filtered empty state)
  emptyState?: ReactNode
}

const MissionListPageContentWrapper: FC<MissionListContentWrapperProps> = ({
  icon,
  title,
  subtitle,
  filters,
  actions,
  list,
  loading,
  hasMissions,
  isOffline,
  emptyState
}) => {
  return (
    <Stack direction={'column'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        {/* Title sits in the same centered column as the filters/list below, so their left edges align. */}
        <FlexboxGrid justify="center" style={{ padding: '3rem 2rem 0 2rem' }}>
          <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
            <Stack direction={'row'} spacing={'0.2rem'} alignItems={'center'}>
              {icon && <Stack.Item alignSelf={'baseline'}>{icon}</Stack.Item>}
              <Stack.Item>
                <Text as={'h1'} style={{ fontSize: '32px' }}>
                  {title}
                </Text>
              </Stack.Item>
            </Stack>
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </Stack.Item>
      {subtitle && (
        <Stack.Item style={{ width: '100%' }}>
          <FlexboxGrid justify="center" style={{ padding: '0 2rem', marginTop: '4rem' }}>
            <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
              <Text as={'h1'}>{subtitle}</Text>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </Stack.Item>
      )}
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="flex-start" spacing="0.2rem" style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%', height: '100%' }}>
            <FlexboxGrid justify="center" style={{ padding: '2rem 2rem 4rem 2rem', display: 'flex', flex: 1 }}>
              <FlexboxGrid.Item as={Col} colspan={24} xxl={20} style={{ marginBottom: '3rem' }}>
                {filters}
              </FlexboxGrid.Item>
              <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
                {loading ? (
                  <div style={{ marginTop: '25rem' }}>
                    <Loader
                      data-testid={'mission-list-loader'}
                      center={true}
                      size={'md'}
                      vertical={true}
                      content={<Text as={'h3'}>Missions en cours de chargement</Text>}
                    />
                  </div>
                ) : isOffline ? (
                  <div style={{ marginTop: '10rem' }}>
                    <Text as={'h3'} style={{ textAlign: 'center' }}>
                      Veuillez repasser en ligne pour resynchroniser.
                    </Text>
                  </div>
                ) : !hasMissions ? (
                  // custom empty state (e.g. filtered no-result) when provided, otherwise the default message
                  (emptyState ?? (
                    <div style={{ marginTop: '10rem' }}>
                      <Text as={'h3'} style={{ textAlign: 'center' }}>
                        Aucune mission pour cette période de temps.
                      </Text>
                    </div>
                  ))
                ) : (
                  <Stack direction={'column'} spacing={'1rem'} style={{ width: '100%' }}>
                    <Stack.Item style={{ width: '100%' }}>{actions}</Stack.Item>
                    <Stack.Item style={{ width: '100%' }}> {list}</Stack.Item>
                  </Stack>
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
