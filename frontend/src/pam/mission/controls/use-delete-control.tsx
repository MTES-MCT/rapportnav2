import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from "../timeline/use-mission-timeline.tsx";
import { useParams } from "react-router-dom";
import { ControlType } from "../../../types/control-types.ts";
import { GET_ACTION_BY_ID } from "../actions/use-action-by-id.tsx";

export const DELETE_CONTROL_ADMINISTRATIVE = gql`
    mutation DeleteControlAdministrative($actionId: String!) {
        deleteControlAdministrative(actionId: $actionId)
    }
`

export const DELETE_CONTROL_NAVIGATION = gql`
    mutation DeleteControlNavigation($actionId: String!) {
        deleteControlNavigation(actionId: $actionId)
    }
`

export const DELETE_CONTROL_SECURITY = gql`
    mutation DeleteControlSecurity($actionId: String!) {
        deleteControlSecurity(actionId: $actionId)
    }
`

export const DELETE_CONTROL_GENS_DE_MER = gql`
    mutation DeleteControlGensDeMer($actionId: String!) {
        deleteControlGensDeMer(actionId: $actionId)
    }
`

const mutations = {
    [ControlType.ADMINISTRATIVE]: DELETE_CONTROL_ADMINISTRATIVE,
    [ControlType.NAVIGATION]: DELETE_CONTROL_NAVIGATION,
    [ControlType.SECURITY]: DELETE_CONTROL_SECURITY,
    [ControlType.GENS_DE_MER]: DELETE_CONTROL_GENS_DE_MER,
}

export interface UseDeleteControlProps {
    controlType: ControlType
}

const useDeleteControl = ({controlType}: UseDeleteControlProps): MutationTuple<boolean, Record<string, any>> => {
    const {missionId} = useParams()
    const mutation = useMutation(mutations[controlType], {
        refetchQueries: [
            {query: GET_MISSION_TIMELINE, variables: {missionId}},
            GET_ACTION_BY_ID
        ]
    })

    return mutation
}

export default useDeleteControl
