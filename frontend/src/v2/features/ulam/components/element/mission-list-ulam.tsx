import React, { JSX } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { Col, Container, FlexboxGrid, Loader, Stack } from 'rsuite'
import { Icon } from '@mtes-mct/monitor-ui'
interface MissionListUlamProps  {
  dateRangeNavigator: JSX.Element,
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
      <FlexboxGrid justify="center" style={{ padding: '4rem 2rem', display: 'flex', flex: 1 }}>
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
            <Stack direction="column" alignItems="flex-start" spacing="0.2rem" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <MissionListHeaderUlam />
              </Stack.Item>
              <Stack.Item style={{ width: '100%', height: '100%' }}>
                {missions?.map((mission, index) => (
                  <MissionListItemUlam
                    mission={mission}
                    index={index}
                    openIndex={openIndex}
                    setOpenIndex={setOpenIndex}
                  />
                ))}
              </Stack.Item>
            </Stack>
          </Container>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </>
  )
}

export default MissionListUlam
