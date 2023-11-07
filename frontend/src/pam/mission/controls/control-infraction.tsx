import React, { useState } from 'react'
import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { Infraction, ControlType } from '../../mission-types'
import InfractionSummary from '../infractions/infraction-summary'
import InfractionForm from '../infractions/infraction-form'
import { infractionButtonTitle } from '../infractions/utils'
import { GET_MISSION_BY_ID, MUTATION_ADD_OR_UPDATE_INFRACTION, MUTATION_DELETE_INFRACTION } from '../queries'
import { useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'
import omit from 'lodash/omit'

export interface ControlInfractionProps {
  controlId?: string
  controlType: ControlType
  infractions?: Infraction[]
}

const ControlInfraction: React.FC<ControlInfractionProps> = ({ controlId, controlType, infractions }) => {
  const { missionId } = useParams()

  const [showInfractionForm, setShowInfractionForm] = useState<boolean>(false)

  const [mutate, { mutateData, mutateLoading, mutateError }] = useMutation(MUTATION_ADD_OR_UPDATE_INFRACTION, {
    refetchQueries: [GET_MISSION_BY_ID]
  })
  const [deleteMutation] = useMutation(MUTATION_DELETE_INFRACTION, {
    refetchQueries: [GET_MISSION_BY_ID]
  })

  const onSubmit = async (data: Infraction) => {
    const mutationData = {
      ...omit(data, '__typename'),
      id: data?.id,
      missionId,
      controlId,
      controlType
    }
    debugger
    await mutate({ variables: { infraction: mutationData } })
    setShowInfractionForm(false)
  }
  const onDelete = async (data: Infraction) => {
    debugger
    await deleteMutation({
      variables: {
        id: data.id!
      }
    })
  }

  return (
    <>
      {!!!infractions?.length && !showInfractionForm ? (
        <Button
          onClick={() => {
            debugger
            setShowInfractionForm(true)
          }}
          accent={Accent.SECONDARY}
          size={Size.NORMAL}
          Icon={Icon.Plus}
          role="add-infraction-button"
          isFullWidth={true}
        >
          {`${infractionButtonTitle(controlType)}`}
        </Button>
      ) : !!infractions?.length && !showInfractionForm ? (
        <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
          <InfractionSummary
            infractions={infractions}
            controlType={controlType}
            onEdit={() => setShowInfractionForm(true)}
            onDelete={onDelete}
          />
        </div>
      ) : (
        <>
          <Label>Ajout d'infraction</Label>
          <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
            <InfractionForm
              infraction={infractions?.[0]} // only 1 infraction for nav and fish
              onCancel={() => setShowInfractionForm(false)}
              onSubmit={onSubmit}
            />
          </div>
        </>
      )}
    </>
  )
}

export default ControlInfraction
