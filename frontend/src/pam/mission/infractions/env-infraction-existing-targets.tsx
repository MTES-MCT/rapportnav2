import React, { useState } from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { ControlType } from '../../../types/control-types'
import { Infraction, InfractionByTarget, InfractionEnvNewTarget } from '../../../types/infraction-types'
import { MUTATION_ADD_OR_UPDATE_INFRACTION_ENV, MUTATION_DELETE_INFRACTION } from '../queries'
import { useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import EnvInfractionSummary from './env-infraction-summary'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import EnvInfractionTargetAddedByCacemForm from "./env-infraction-target-added-by-cacem-form.tsx";
import EnvInfractionTargetAddedByUnitForm from "./env-infraction-target-added-by-unit-form.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";
import Text from '../../../ui/text'


export interface EnvInfractionExistingTargetProps {
    availableControlTypesForInfraction?: ControlType[]
    infractionsByTarget?: InfractionByTarget[]
}

const EnvInfractionExistingTargets: React.FC<EnvInfractionExistingTargetProps> = ({
                                                                                      availableControlTypesForInfraction,
                                                                                      infractionsByTarget
                                                                                  }) => {
    const {missionId, actionId} = useParams()

    const [selectedVessel, setSelectedVessel] = useState<string | undefined>(undefined)

    const [formData, setFormData] = useState<Infraction | undefined>(undefined) // only 1 infraction for nav and fish

    const [mutate, {data, error: updateError, loading}] = useMutation(MUTATION_ADD_OR_UPDATE_INFRACTION_ENV, {
        refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
    })

    const [deleteMutation] = useMutation(MUTATION_DELETE_INFRACTION, {
        refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
    })

    const onChangeFormField = (field: string, value: any) => {
        setFormData((prevData: any) => ({...prevData, [field]: value}))
    }

    const onChangeTargetFormField = (field: string, value: any) => {
        setFormData((prevData: any) => ({...prevData, target: {...prevData.target, [field]: value}}))
    }

    const onSubmit = async (e: React.FormEvent, infraction?: Infraction) => {
        e.preventDefault()
        const mutationData = {
            ...omit(infraction, '__typename', 'target'),
            id: infraction?.id,
            missionId,
            actionId,
            // controlType: infraction?.controlType,
            vesselType: infraction?.target?.vesselType,
            vesselSize: infraction?.target?.vesselSize,
            vesselIdentifier: infraction?.target?.vesselIdentifier,
            identityControlledPerson: infraction?.target?.identityControlledPerson
            // controlId,
            // controlType
        } as unknown as InfractionEnvNewTarget
        await mutate({variables: {infraction: mutationData}})
        setSelectedVessel(undefined)
    }

    const deleteInfraction = async (infractionId: string) => {
        await deleteMutation({
            variables: {
                id: infractionId
            }
        })
    }

    return (
        <>
            {!!infractionsByTarget?.length &&
                infractionsByTarget?.map((infractionByTarget: InfractionByTarget, i: number) => (
                    <div key={infractionByTarget.vesselIdentifier} style={{
                        width: '100%',
                        backgroundColor: THEME.color.white,
                        padding: '1rem',
                        marginBottom: '0.25rem'
                    }}>
                        {selectedVessel === infractionByTarget.vesselIdentifier ? (
                            <>

                                <form onSubmit={(e: React.FormEvent) => onSubmit(e, formData)}>
                                    {
                                        infractionByTarget.targetAddedByUnit ? (
                                                <EnvInfractionTargetAddedByUnitForm
                                                    infraction={formData}
                                                    onChange={onChangeFormField}
                                                    onChangeTarget={onChangeTargetFormField}
                                                    availableControlTypesForInfraction={
                                                        availableControlTypesForInfraction?.filter(controlType => !infractionByTarget.controlTypesWithInfraction?.includes(controlType))
                                                    }
                                                    onCancel={() => {
                                                        setFormData(undefined)
                                                        setSelectedVessel(undefined)
                                                    }}
                                                />

                                            )

                                            : (
                                                <EnvInfractionTargetAddedByCacemForm
                                                    infraction={infractionByTarget}
                                                    formData={formData}
                                                    onChange={onChangeFormField}
                                                    onCancel={() => {
                                                        setFormData(undefined)
                                                        setSelectedVessel(undefined)
                                                    }}
                                                    availableControlTypesForInfraction={
                                                        availableControlTypesForInfraction?.filter(controlType => !infractionByTarget.controlTypesWithInfraction?.includes(controlType))
                                                    }
                                                />
                                            )
                                    }
                                    {
                                        !!updateError && (
                                            <div style={{padding: '0 1rem'}}>
                                                <Text as={'h3'} weight={'medium'}
                                                      color={THEME.color.maximumRed}>ERREUR: {updateError?.message}</Text>
                                            </div>
                                        )
                                    }
                                </form>
                            </>
                        ) : (
                            <EnvInfractionSummary
                                infractionByTarget={infractionByTarget}
                                onAddInfractionForTarget={(infraction?: Partial<Infraction>) => {
                                    setFormData(infraction as Infraction)
                                    setSelectedVessel(infractionByTarget.vesselIdentifier)
                                }}
                                onEditInfractionForTarget={(infraction: Infraction) => {
                                    setFormData(infraction)
                                    setSelectedVessel(infractionByTarget.vesselIdentifier)
                                }}
                                onDeleteInfraction={(infractionId: string) => deleteInfraction(infractionId)}
                            />
                        )}
                    </div>
                ))}
        </>
    )
}

export default EnvInfractionExistingTargets
