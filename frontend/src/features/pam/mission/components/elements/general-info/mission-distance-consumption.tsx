import { Label, NumberInput, THEME } from '@mtes-mct/monitor-ui'
import React, { useState } from 'react'
import { useParams } from 'react-router-dom'
import { Stack } from 'rsuite'
import { MissionGeneralInfo } from '@common/types/mission-types.ts'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'
import useAddOrUpdateGeneralInfo from '../../../hooks/use-add-update-distance-consumption.tsx'
import { isNil } from 'lodash'

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
  const [mutate] = useAddOrUpdateGeneralInfo()

  const onChange = async (field: string, value?: number) => {
    const updatedValue = value !== undefined && value !== null ? value : undefined
    setFormData({
      ...formData,
      [field]: updatedValue
    })
  }

  const onBlur = async () => {
    let updatedData = {
      ...info,
      missionId,
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
            error={isMissionFinished && isNil(formData?.distanceInNauticalMiles) ? 'error' : undefined}
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
            error={isMissionFinished && isNil(formData?.consumedGOInLiters) ? 'error' : undefined}
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
            error={isMissionFinished && isNil(formData?.consumedFuelInLiters) ? 'error' : undefined}
            onChange={(nextValue?: number) => onChange('consumedFuelInLiters', nextValue)}
            onBlur={() => onBlur()}
          />
        </Stack.Item>
      </Stack>
    </>
  )
}

export default MissionDistanceAndConsumption
