import React from 'react'
import { Stack } from 'rsuite'
import MissionInfractionEnvSummaryByCacemForm from './mission-infraction-env-summary-by-cacem'

interface MissionInfractionEnvSummaryProps {}

const MissionInfractionEnvSummary: React.FC<MissionInfractionEnvSummaryProps> = () => {
  return (
    <Stack>
      <MissionInfractionEnvSummaryByCacemForm />
    </Stack>
  )
}

export default MissionInfractionEnvSummary
