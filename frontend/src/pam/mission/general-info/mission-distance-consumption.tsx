import React, { useState } from 'react'
import { Stack } from 'rsuite'
import { Label, NumberInput, THEME } from '@mtes-mct/monitor-ui'
import { useParams } from 'react-router-dom'
import { MissionGeneralInfo } from '../../../types/mission-types.ts'
import useAddOrUpdateDistanceConsumption from './use-add-update-distance-consumption.tsx'
import useIsMissionFinished from '../use-is-mission-finished.tsx'

interface MissionDistanceAndConsumptionProps {
  info?: MissionGeneralInfo
}

const MissionDistanceAndConsumption: React.FC<MissionDistanceAndConsumptionProps> = ({ info }) => {
  const { missionId } = useParams()
  const [formData, setFormData] = useState({
    distanceInNauticalMiles: info?.distanceInNauticalMiles,
    consumedGOInLiters: info?.consumedGOInLiters,
    consumedFuelInLiters: info?.consumedFuelInLiters
  })

  const isMissionFinished = useIsMissionFinished(missionId)
  const [mutate] = useAddOrUpdateDistanceConsumption()

  const onChange = async (field: string, value?: number) => {
    const updatedValue = value !== undefined && value !== null ? value?.toString() : undefined
    setFormData({
      ...formData,
      [field]: updatedValue
    })
  }

  const onBlur = async () => {
    let updatedData = {
      id: info?.id,
      missionId: missionId,
      ...formData
    }

    await mutate({ variables: { info: updatedData } })
  }

  return (
    <>
      <Label>Distance et consommation</Label>
      <Stack
        direction="row"
        alignItems="flex-start"
        spacing="1rem"
        style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}
      >
        <Stack.Item style={{ flex: 1 }}>
          <NumberInput
            label="Distance parcourue en milles"
            name="distanceInNauticalMiles"
            role="distanceInNauticalMiles"
            isRequired={true}
            placeholder="0"
            isLight={true}
            value={formData?.distanceInNauticalMiles}
            isErrorMessageHidden={true}
            error={isMissionFinished && !formData?.distanceInNauticalMiles ? 'error' : undefined}
            onChange={(nextValue?: number) => onChange('distanceInNauticalMiles', nextValue)}
            onBlur={() => onBlur()}
          />
        </Stack.Item>
        <Stack.Item style={{ flex: 1 }}>
          <NumberInput
            label="GO marine consommé en litres"
            name="consumedGOInLiters"
            role="consumedGOInLiters"
            isRequired={true}
            placeholder="0"
            isLight={true}
            value={formData?.consumedGOInLiters}
            isErrorMessageHidden={true}
            error={isMissionFinished && !formData?.consumedGOInLiters ? 'error' : undefined}
            onChange={(nextValue?: number) => onChange('consumedGOInLiters', nextValue)}
            onBlur={() => onBlur()}
          />
        </Stack.Item>
        <Stack.Item style={{ flex: 1 }}>
          <NumberInput
            label="Essence consommée en litres"
            name="consumedFuelInLiters"
            role="consumedFuelInLiters"
            isRequired={true}
            placeholder="0"
            isLight={true}
            value={formData?.consumedFuelInLiters}
            isErrorMessageHidden={true}
            error={isMissionFinished && !formData?.consumedFuelInLiters ? 'error' : undefined}
            onChange={(nextValue?: number) => onChange('consumedFuelInLiters', nextValue)}
            onBlur={() => onBlur()}
          />
        </Stack.Item>
      </Stack>
    </>
  )
}

export default MissionDistanceAndConsumption
