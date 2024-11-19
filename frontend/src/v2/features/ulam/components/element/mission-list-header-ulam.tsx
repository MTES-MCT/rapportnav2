import React from 'react'
import { FlexboxGrid } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'

interface MissionListHeaderProps {}

const MissionListHeaderUlam: React.FC<MissionListHeaderProps> = ({}) => {
  return (
    <FlexboxGrid align="middle" style={{ height: '100%', padding: '0.5rem 1rem' }}>
      <FlexboxGrid.Item colspan={1} style={{ paddingTop: '8px' }}></FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={3} style={{ paddingTop: '8px' }}></FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={3}></FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={2}>
        <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Date d'ouverture</p>
      </FlexboxGrid.Item>
      <>
        <FlexboxGrid.Item colspan={4}>
          <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Moyen(s) utilisé(s)</p>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item colspan={4}>
          <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Agent(s)</p>
        </FlexboxGrid.Item>
      </>
      <FlexboxGrid.Item colspan={2}>
        <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Statut mission</p>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={3}>
        <p style={{ color: THEME.color.slateGray, fontSize: '12px' }}>Statut du rapport</p>
      </FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={3}></FlexboxGrid.Item>
      <FlexboxGrid.Item colspan={1}></FlexboxGrid.Item>
    </FlexboxGrid>
  )
}

export default MissionListHeaderUlam
