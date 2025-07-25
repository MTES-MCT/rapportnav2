import Text from '@common/components/ui/text.tsx'
import { Accent, Icon, IconButton, Size, TagGroup, THEME } from '@mtes-mct/monitor-ui'
import React, { JSX } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import styled from 'styled-components'

const StyledHeader = styled.div`
  height: 60px;
  background: #3b4559 0 0 no-repeat padding-box;
  opacity: 1;
  padding: 0 2rem;
`

interface PageHeaderProps {
  tags?: JSX.Element
  date?: JSX.Element
  utcTime?: JSX.Element
  banner?: JSX.Element
  onClickClose: () => void
}

const PageHeaderWrapper: React.FC<PageHeaderProps> = ({ tags, date, banner, utcTime, onClickClose }) => {
  return (
    <>
      <StyledHeader>
        <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
          <FlexboxGrid.Item>
            <Stack direction="row" spacing={'1rem'}>
              <Stack.Item>
                <Text as="h1" weight="bold" color={THEME.color.gainsboro}>
                  {date}
                </Text>
              </Stack.Item>

              <Stack.Item>
                <TagGroup>{tags}</TagGroup>
              </Stack.Item>
            </Stack>
          </FlexboxGrid.Item>
          <FlexboxGrid.Item colspan={6}>
            <FlexboxGrid justify="end" align="middle" style={{ height: '100%' }}>
              <Stack direction={'row'} alignItems={'center'} spacing={'2rem'}>
                <Stack.Item>{utcTime}</Stack.Item>

                <Stack.Item>
                  <IconButton
                    Icon={Icon.Close}
                    accent={Accent.TERTIARY}
                    size={Size.NORMAL}
                    color={THEME.color.gainsboro}
                    onClick={onClickClose}
                    role={'quit-mission-cross'}
                    style={{ marginTop: '4px' }}
                  />
                </Stack.Item>
              </Stack>
            </FlexboxGrid>
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </StyledHeader>
      {banner}
    </>
  )
}

export default PageHeaderWrapper
