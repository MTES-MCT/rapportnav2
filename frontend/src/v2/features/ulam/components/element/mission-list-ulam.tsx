import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Col, FlexboxGrid, Loader, Stack } from 'rsuite'
import MissionListing from '../../../common/components/elements/mission-listing.tsx'
import MissionListDaterangeNavigator from '../../../common/components/elements/mission-list-daterange-navigator.tsx'

const MissionListUlam: React.FC = () => {
  const navigate = useNavigate()
  return (
    <>
      <FlexboxGrid
        // align="middle"
        justify="center"
        style={{ padding: '4rem 2rem', display: 'flex', flex: 1 }}
      >
        <FlexboxGrid.Item as={Col} colspan={24} xxl={23}>
          <MissionListDaterangeNavigator />
          <MissionListing/>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </>
  )
}

export default MissionListUlam
