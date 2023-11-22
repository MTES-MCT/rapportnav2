import React from 'react'
import styled from 'styled-components'
import { FlexboxGrid } from 'rsuite'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../ui/text'

const StyledHeader = styled.div`
  height: 60px;
  background: var(--charcoal-3b4559-) 0% 0% no-repeat padding-box;
  background: #3b4559 0% 0% no-repeat padding-box;
  opacity: 1;
  padding: 0 2rem;
`

interface MissionPageHeaderProps {
  missionName: string
  onClickClose: () => void
}

const MissionPageHeader: React.FC<MissionPageHeaderProps> = ({ missionName, onClickClose }) => {
  return (
    <StyledHeader>
      <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
        <FlexboxGrid.Item>
          <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
            <FlexboxGrid.Item>
              <Text as="h1" weight="bold" color={THEME.color.gainsboro}>
                {missionName}
              </Text>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item colspan={2}>
          <FlexboxGrid justify="end" align="middle" style={{ height: '100%' }}>
            <IconButton
              Icon={Icon.Close}
              accent={Accent.TERTIARY}
              size={Size.NORMAL}
              color={THEME.color.gainsboro}
              onClick={onClickClose}
            />
          </FlexboxGrid>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </StyledHeader>
  )
}

export default MissionPageHeader
