import { Button, Size } from '@mtes-mct/monitor-ui'
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
          <Button onClick={() => navigate(`/ulam/missions/761`)} size={Size.NORMAL}>
            Mission Ulam details
          </Button>
        </Stack.Item>
      </Stack>
    </>
  )
}

export default MissionListUlam
