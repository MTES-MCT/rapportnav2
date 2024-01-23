import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";
import { InfractionByTarget } from "../../../types/infraction-types.ts";

export const MUTATION_ADD_OR_UPDATE_INFRACTION_ENV = gql`
    mutation AddOrUpdateInfractionForEnvTarget($infraction: InfractionWithNewTargetInput!) {
        addOrUpdateInfractionForEnvTarget(infraction: $infraction) {
            id
            controlType
            infractionType
            natinfs
            observations
            target {
                id
                identityControlledPerson
                vesselIdentifier
                vesselSize
                vesselType
            }
        }
    }
`


const useAddOrUpdateInfractionEnv = (): MutationTuple<InfractionByTarget, Record<string, any>> => {
    const mutation = useMutation(MUTATION_ADD_OR_UPDATE_INFRACTION_ENV, {
        refetchQueries: [GET_MISSION_TIMELINE, GET_ACTION_BY_ID]
    })

    return mutation
}

export default useAddOrUpdateInfractionEnv
