import React from 'react'
import styled from 'styled-components'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Icon, Button, Size, THEME } from '@mtes-mct/monitor-ui'
import { useNavigate } from 'react-router-dom'

const StyledFooter = styled.div`
  height: 62px;
  background: var(--white-ffffff-) 0% 0% no-repeat padding-box;
  background: #ffffff 0% 0% no-repeat padding-box;
  border-top: 1px solid var(--lightGray-cccfd6-);
  border-top: 1px solid #cccfd6;
  padding: 0 2rem;
`

interface MissionPageFooterProps {
  missionName: string
  exitMission: () => void
}

const MissionPageFooter: React.FC<MissionPageFooterProps> = ({ missionName, exitMission }) => {
  const navigate = useNavigate()

  const deleteMission = () => {
    // TODO add delete
    alert('Fonctionnalité pas encore implémentée')
    exitMission()
  }
  const saveAndQuitMission = () => {
    // TODO add save
    alert('Fonctionnalité pas encore implémentée')
    exitMission()
  }
  const finishMission = () => {
    // TODO add state machine change
    alert('Fonctionnalité pas encore implémentée')
    exitMission()
  }

  return (
    <StyledFooter>
      <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
        <FlexboxGrid.Item>
          <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
            <FlexboxGrid.Item>
              <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Delete} onClick={deleteMission}>
                Supprimer la mission
              </Button>
            </FlexboxGrid.Item>
          </FlexboxGrid>
        </FlexboxGrid.Item>
        <FlexboxGrid.Item>
          <Stack direction="row">
            <Stack.Item style={{ paddingLeft: '1rem' }}>
              <Button accent={Accent.TERTIARY} size={Size.NORMAL} onClick={exitMission}>
                Quitter
              </Button>
            </Stack.Item>
            <Stack.Item style={{ paddingLeft: '1rem' }}>
              <Button accent={Accent.PRIMARY} size={Size.NORMAL} Icon={Icon.Save} onClick={saveAndQuitMission}>
                Enregistrer et quitter
              </Button>
            </Stack.Item>
            <Stack.Item style={{ paddingLeft: '1rem' }}>
              <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Check} onClick={finishMission}>
                Clôturer la mission
              </Button>
            </Stack.Item>
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </StyledFooter>
  )
}

export default MissionPageFooter
