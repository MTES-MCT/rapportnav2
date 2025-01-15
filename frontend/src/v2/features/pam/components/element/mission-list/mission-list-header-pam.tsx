import Text from '@common/components/ui/text.tsx'
import { THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { FlexboxGrid } from 'rsuite'

interface MissionListHeaderProps {}

const MissionListHeaderPam: React.FC<MissionListHeaderProps> = () => {
  return (
    <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 2rem' }}>
      <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }}></FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={4} style={{ paddingTop: '8px' }}></FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={4}></FlexboxGrid.Item>
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
      <FlexboxGrid.Item colspan={1}>
        <Text as={'h4'} color={THEME.color.slateGray}></Text>
      </FlexboxGrid.Item>

      <FlexboxGrid.Item colspan={3}>
        <Text as={'h4'} color={THEME.color.slateGray}>
          Statut mission
        </Text>
      </FlexboxGrid.Item>

      <FlexboxGrid.Item colspan={3}>
        <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>
          <Text as={'h4'} color={THEME.color.slateGray}>
            Statut du rapport
          </Text>
        </p>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={1}>
        <Text as={'h4'} color={THEME.color.slateGray}></Text>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
    </FlexboxGrid>
  )
}

export default MissionListHeaderPam
