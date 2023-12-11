import React from 'react'
import {Stack} from 'rsuite'
import {THEME, Label, NumberInput} from '@mtes-mct/monitor-ui'
import {MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION} from '../queries'
import {useMutation} from '@apollo/client'
import omit from 'lodash/omit'
import {useParams} from 'react-router-dom'
import {GET_MISSION_EXCERPT} from "./use-mission-excerpt.tsx";

interface MissionDistanceAndConsumptionProps {
    info?: MissionDistanceAndConsumption
}

const MissionDistanceAndConsumption: React.FC<MissionDistanceAndConsumptionProps> = ({info}) => {
    const {missionId} = useParams()

    const [mutate, {statusData, statusLoading, statusError}] = useMutation(
        MUTATION_ADD_OR_UPDATE_DISTANCE_CONSUMPTION,
        {
            refetchQueries: [GET_MISSION_EXCERPT]
        }
    )

    const onChange = async (field?: string, value?: number) => {
        let updatedData = {
            ...omit(info, '__typename', 'infractions'),
            id: info?.id,
            missionId: missionId
        }

        if (!!field && !!value) {
            updatedData = {
                ...updatedData,
                [field]: value.toString()
            }
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
                        value={info?.distanceInNauticalMiles}
                        onChange={(nextValue?: number) => onChange('distanceInNauticalMiles', nextValue)}
                    />
                </Stack.Item>
                <Stack.Item style={{flex: 1}}>
                    <NumberInput
                        label="GO marine consommé en litres"
                        name="consumedGOInLiters"
                        placeholder="0"
                        isLight={true}
                        value={info?.consumedGOInLiters}
                        onChange={(nextValue?: number) => onChange('consumedGOInLiters', nextValue)}
                    />
                </Stack.Item>
                <Stack.Item style={{flex: 1}}>
                    <NumberInput
                        label="Essence consommée en litres"
                        name="consumedFuelInLiters"
                        placeholder="0"
                        isLight={true}
                        value={info?.consumedFuelInLiters}
                        onChange={(nextValue?: number) => onChange('consumedFuelInLiters', nextValue)}
                    />
                </Stack.Item>
            </Stack>
        </>
    )
}

export default MissionDistanceAndConsumption
