import React, {useState} from 'react'
import {Stack} from 'rsuite'
import {THEME, Label, NumberInput} from '@mtes-mct/monitor-ui'
import {MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION} from '../queries'
import {useMutation} from '@apollo/client'
import {useParams} from 'react-router-dom'
import {GET_MISSION_EXCERPT} from "./use-mission-excerpt.tsx";
import {MissionGeneralInfo} from "../../../types/mission-types.ts";

interface MissionDistanceAndConsumptionProps {
    info?: MissionGeneralInfo
}

const MissionDistanceAndConsumption: React.FC<MissionDistanceAndConsumptionProps> = ({info}) => {
    const {missionId} = useParams()
    const [formData, setFormData] = useState({
        distanceInNauticalMiles: info?.distanceInNauticalMiles,
        consumedGOInLiters: info?.consumedGOInLiters,
        consumedFuelInLiters: info?.consumedFuelInLiters
    })

    const [mutate] = useMutation(
        MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION,
        {
            refetchQueries: [GET_MISSION_EXCERPT]
        }
    )

    const onChange = async (field: string, value?: number) => {
        setFormData({
            ...formData,
            [field]: value?.toString()
        })
    }

    const onBlur = async () => {
        let updatedData = {
            id: info?.id,
            missionId: missionId,
            ...formData
        }

        await mutate({variables: {info: updatedData}})
    }

    return (
        <>
            <Label>Distance et consommation</Label>
            <Stack
                direction="row"
                alignItems="flex-start"
                spacing="1rem"
                style={{width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem'}}
            >
                <Stack.Item style={{flex: 1}}>
                    <NumberInput
                        label="Distance parcourue en milles"
                        name="distanceInNauticalMiles"
                        placeholder="0"
                        isLight={true}
                        value={formData?.distanceInNauticalMiles}
                        onChange={(nextValue?: number) => onChange('distanceInNauticalMiles', nextValue)}
                        onBlur={() => onBlur()}
                    />
                </Stack.Item>
                <Stack.Item style={{flex: 1}}>
                    <NumberInput
                        label="GO marine consommé en litres"
                        name="consumedGOInLiters"
                        placeholder="0"
                        isLight={true}
                        value={formData?.consumedGOInLiters}
                        onChange={(nextValue?: number) => onChange('consumedGOInLiters', nextValue)}
                        onBlur={() => onBlur()}
                    />
                </Stack.Item>
                <Stack.Item style={{flex: 1}}>
                    <NumberInput
                        label="Essence consommée en litres"
                        name="consumedFuelInLiters"
                        placeholder="0"
                        isLight={true}
                        value={formData?.consumedFuelInLiters}
                        onChange={(nextValue?: number) => onChange('consumedFuelInLiters', nextValue)}
                        onBlur={() => onBlur()}
                    />
                </Stack.Item>
            </Stack>
        </>
    )
}

export default MissionDistanceAndConsumption
