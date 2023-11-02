import React from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Button, Icon, IconButton, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import {
  Mission,
  Action,
  getActionData,
  getActionStartTime,
  ActionStatusType,
  Infraction,
  ControlType
} from '../../mission-types'
import InfractionSummary from '../infractions/infraction-summary'
import InfractionForm from '../infractions/infraction-form'
import { infractionTitleForControlType } from '../infractions/utils'
import { GET_MISSION_BY_ID, MUTATION_ADD_OR_UPDATE_INFRACTION } from '../queries'
import { useMutation } from '@apollo/client'
import { useParams } from 'react-router-dom'

interface ControlInfractionProps {
  controlId?: string
  controlType: ControlType
  showInfractions: boolean
  infraction?: Infraction
  showInfractionForm: (show: boolean) => void
}

const ControlInfraction: React.FC<ControlInfractionProps> = ({
  controlId,
  controlType,
  showInfractions,
  infraction,
  showInfractionForm
}) => {
  const { missionId } = useParams()
  const [mutate, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_INFRACTION, {
    refetchQueries: [GET_MISSION_BY_ID]
  })

  const onSubmit = async (data: any) => {
    const mutationData = {
      ...data,
      missionId,
      controlId,
      controlType
    }
    debugger
    await mutate({ variables: { infraction: mutationData } })
  }

  return (
    <>
      {!!infraction ? (
        <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
          <InfractionSummary infraction={infraction} controlType={controlType} />
        </div>
      ) : showInfractions ? (
        <>
          <Label>Ajout d'infraction</Label>
          <div style={{ width: '100%', backgroundColor: THEME.color.cultured, padding: '1rem' }}>
            <InfractionForm onCancel={() => showInfractionForm(false)} onSubmit={onSubmit} />
          </div>
        </>
      ) : (
        <Button
          onClick={() => showInfractionForm(true)}
          accent={Accent.SECONDARY}
          size={Size.NORMAL}
          Icon={Icon.Plus}
          isFullWidth
        >
          {`Ajouter une ${infractionTitleForControlType(controlType)}`}
        </Button>
      )}
    </>
  )
}

export default ControlInfraction
