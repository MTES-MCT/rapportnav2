import { Button, Size } from '@mtes-mct/monitor-ui'
import { ULAM_V2_HOME_PATH } from '@router/router'
import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'

const MissionListUlam: React.FC = () => {
  const navigate = useNavigate()
  return (
    <>
      <Stack>
        <Stack.Item>MISSION LIST ULAM</Stack.Item>
        <Stack.Item>
          <Button onClick={() => navigate(`${ULAM_V2_HOME_PATH}/761`)} size={Size.NORMAL}>
            Mission Ulam details
          </Button>
        </Stack.Item>
      </Stack>
    </>
  )
}

export default MissionListUlam
