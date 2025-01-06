import React, { JSX, useState } from 'react'
import { Col, Container, FlexboxGrid } from 'rsuite'
import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import MissionCreateDialog from './mission-create-dialog.tsx'
import { MissionTypeEnum } from '../../../common/types/mission-types.ts'
import { MissionSourceEnum } from '@common/types/env-mission-types.ts'

interface MissionListUlamProps {
  dateRangeNavigator: JSX.Element
  missionListing: JSX.Element
}

const MissionListUlam: React.FC<MissionListUlamProps> = ({ dateRangeNavigator, missionListing }) => {
  const [isDialogOpen, setIsDialogOpen] = useState(false)

  const handleCloseDialog = () => {
    setIsDialogOpen(false)
  }

 // const [createMission] = useCreateMissionMutation()

  const handleCreateMission = async () => {
    const createMissionBody = {
      startDateTimeUtc: new Date().toISOString(),
      endDateTimeUtc: new Date().toISOString(),
      openBy: 'rapportnav-dev',
      missionTypes: [MissionTypeEnum.AIR],
      missionSource: MissionSourceEnum.RAPPORTNAV
    }


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
                <Button accent={Accent.PRIMARY} onClick={() => handleCreateMission()}>Créer un rapport</Button>
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
