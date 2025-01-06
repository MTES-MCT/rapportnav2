import React, { JSX, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Col, Container, FlexboxGrid } from 'rsuite'
import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import MissionCreateDialog from './mission-create-dialog.tsx'

interface MissionListUlamProps {
  dateRangeNavigator: JSX.Element
  missionListing: JSX.Element
}

const MissionListUlam: React.FC<MissionListUlamProps> = ({ dateRangeNavigator, missionListing }) => {
  const navigate = useNavigate()

  const [isDialogOpen, setIsDialogOpen] = useState(false)

  const handleCloseDialog = () => {
    setIsDialogOpen(false)
  }

  return (
    <>
      <FlexboxGrid
        justify="center"
        style={{ padding: '4rem 2rem', display: 'flex', flex: 1 }}
      >
        <FlexboxGrid.Item as={Col} colspan={24} xxl={20}>
          <Container>
            <FlexboxGrid justify="space-between" align="middle" style={{ marginBottom: '45px' }} >
              <FlexboxGrid.Item>
                <h3>
                  <Icon.MissionAction size={26} /> Missions
                </h3>
                <h4 style={{ marginBottom: '20px' }}>Mes rapports de mission</h4>
              </FlexboxGrid.Item>
              <FlexboxGrid.Item>
                <Button accent={Accent.PRIMARY} onClick={() => setIsDialogOpen(true)}>Créer un rapport</Button>
              </FlexboxGrid.Item>
            </FlexboxGrid>

            <MissionCreateDialog isOpen={isDialogOpen} onClose={handleCloseDialog} />
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
