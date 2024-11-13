import React, { JSX } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Col, Container, FlexboxGrid, Loader, Stack } from 'rsuite'
import { Icon } from '@mtes-mct/monitor-ui'
interface MissionListUlamProps  {
  dateRangeNavigator: JSX.Element,
  missionListing: JSX.Element
}
const MissionListUlam: React.FC<MissionListUlamProps> = ({dateRangeNavigator, missionListing}) => {
  const navigate = useNavigate()
  return (
    <>
      <FlexboxGrid
        justify="center"
        style={{ padding: '4rem 2rem', display: 'flex', flex: 1 }}
      >
        <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
          <Container>
            <h3 style={{marginBottom: '45px'}}><Icon.MissionAction size={26}/> Missions</h3>
            <h4 style={{marginBottom: '25px'}}>Mes rapports de mission</h4>
          </Container>
          {dateRangeNavigator}
          <Container>
            {missionListing}
          </Container>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </>
  )
}

export default MissionListUlam
