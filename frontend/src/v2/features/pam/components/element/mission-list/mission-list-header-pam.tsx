import Text from '@common/components/ui/text.tsx'
import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { FlexboxGrid } from 'rsuite'
import MissionListCountTag from '../../../../common/components/elements/mission-list-count-tag.tsx'

interface MissionListHeaderProps {
  count: number
}

const MissionListHeaderPam: React.FC<MissionListHeaderProps> = ({ count }) => {
  return (
    <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 2rem' }}>
      <FlexboxGrid.Item colspan={9}>
        <MissionListCountTag count={count} />
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={2}>
        <Text as={'h4'} color={THEME.color.slateGray}>
          Date de début
        </Text>
      </FlexboxGrid.Item>

      <FlexboxGrid.Item colspan={2}>
        <Text as={'h4'} color={THEME.color.slateGray}>
          Date de fin
        </Text>
      </FlexboxGrid.Item>

      <FlexboxGrid.Item colspan={2}>
        <Text as={'h4'} color={THEME.color.slateGray} style={{ textAlign: 'center' }}>
          Bordée
        </Text>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>

      <FlexboxGrid.Item colspan={3}>
        <Text as={'h4'} color={THEME.color.slateGray}>
          Statut mission
        </Text>
      </FlexboxGrid.Item>

      <FlexboxGrid.Item colspan={3}>
        <Text as={'h4'} color={THEME.color.slateGray}>
          Statut du rapport
        </Text>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
    </FlexboxGrid>
  )
}

export default MissionListHeaderPam
