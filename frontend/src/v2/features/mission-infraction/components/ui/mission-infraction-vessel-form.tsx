import { FormikSelect } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import { useVessel } from '../../../common/hooks/use-vessel.tsx'

interface MissionInfractionVesselFormProps {
  size: string
  type: string
}

const MissionInfractionVesselForm: React.FC<MissionInfractionVesselFormProps> = ({ type, size }) => {
  const { vesselSizeOptions, vesselTypeOptions } = useVessel()
  return (
    <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
      <Stack.Item style={{ width: '40%' }}>
        <FormikSelect name={size} isRequired={true} label="Taille du navire" options={vesselSizeOptions} />
      </Stack.Item>
      <Stack.Item style={{ width: '60%' }}>
        <FormikSelect isRequired={true} name={type} label="Type de navire" options={vesselTypeOptions} />
      </Stack.Item>
    </Stack>
  )
}

export default MissionInfractionVesselForm
