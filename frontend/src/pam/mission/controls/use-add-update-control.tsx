import { gql, MutationTuple, useMutation } from '@apollo/client'
import { GET_MISSION_TIMELINE } from '../timeline/use-mission-timeline.tsx'
import { ActionControl } from '../../../types/action-types.ts'
import { useParams } from 'react-router-dom'
import { ControlType } from '../../../types/control-types.ts'
import { GET_ACTION_BY_ID } from '../actions/use-action-by-id.tsx'

export const MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER = gql`
  mutation AddOrUpdateControlGensDeMer($control: ControlGensDeMerInput!) {
    addOrUpdateControlGensDeMer(control: $control) {
      id
      amountOfControls
      unitShouldConfirm
      unitHasConfirmed
      staffOutnumbered
      upToDateMedicalCheck
      knowledgeOfFrenchLawAndLanguage
      observations
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_NAVIGATION = gql`
  mutation AddOrUpdateControlNavigation($control: ControlNavigationInput!) {
    addOrUpdateControlNavigation(control: $control) {
      id
      amountOfControls
      unitShouldConfirm
      unitHasConfirmed
      observations
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY = gql`
  mutation AddOrUpdateControlSecurity($control: ControlSecurityInput!) {
    addOrUpdateControlSecurity(control: $control) {
      id
      amountOfControls
      unitShouldConfirm
      unitHasConfirmed
      observations
    }
  }
`

export const MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE = gql`
  mutation AddOrUpdateControlAdministrative($control: ControlAdministrativeInput!) {
    addOrUpdateControlAdministrative(control: $control) {
      id
      amountOfControls
      unitShouldConfirm
      unitHasConfirmed
      compliantOperatingPermit
      upToDateNavigationPermit
      compliantSecurityDocuments
      observations
    }
  }
`

const mutations = {
  [ControlType.ADMINISTRATIVE]: MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE,
  [ControlType.NAVIGATION]: MUTATION_ADD_OR_UPDATE_CONTROL_NAVIGATION,
  [ControlType.SECURITY]: MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY,
  [ControlType.GENS_DE_MER]: MUTATION_ADD_OR_UPDATE_CONTROL_GENS_DE_MER
}

export interface UseAddOrUpdateControlProps {
  controlType: ControlType
}

const useAddOrUpdateControl = ({
  controlType
}: UseAddOrUpdateControlProps): MutationTuple<ActionControl, Record<string, any>> => {
  const { missionId } = useParams()
  const mutation = useMutation(mutations[controlType], {
    refetchQueries: [{ query: GET_MISSION_TIMELINE, variables: { missionId } }, GET_ACTION_BY_ID],
    awaitRefetchQueries: true
  })

  return mutation
}

export default useAddOrUpdateControl
