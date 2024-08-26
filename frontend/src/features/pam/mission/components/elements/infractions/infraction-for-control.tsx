import React, { useEffect, useState } from 'react'
import { Accent, Button, Icon, Label, Size, THEME } from '@mtes-mct/monitor-ui'
import { ControlType } from '../../../../../common/types/control-types.ts'
import { Infraction } from '../../../../../common/types/infraction-types.ts'
import InfractionSummary from './infraction-summary.tsx'
import InfractionForm, { InfractionFormData } from './infraction-form.tsx'
import { infractionButtonTitle } from '../../../utils/infraction-utils.ts'
import { useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import useDeleteInfraction from '../../../hooks/use-delete-infraction.tsx'
import useAddOrUpdateInfraction from '../../../hooks/use-add-update-infraction.tsx'

export interface ControlInfractionProps {
  controlId?: string
  controlType: ControlType
  infractions?: Infraction[]
}

const ControlInfraction: React.FC<ControlInfractionProps> = ({ controlId, controlType, infractions }) => {
  const { missionId, actionId } = useParams()

  const [showInfractionForm, setShowInfractionForm] = useState<boolean>(false)
  const [formData, setFormData] = useState<InfractionFormData | undefined>(undefined) // only 1 infraction for nav and fish

  useEffect(() => {
    setFormData({ ...infractions?.[0], controlType })
  }, [infractions, controlType])

  const onChangeFormField = (field: string, value: any) => {
    setFormData((prevData: any) => ({ ...prevData, [field]: value }))
  }

  const [mutate] = useAddOrUpdateInfraction()
  const [deleteMutation] = useDeleteInfraction()

  const onSubmit = async (e: React.FormEvent, infraction?: Infraction) => {
    e.preventDefault()
    const mutationData = {
      ...omit(infraction, '__typename', 'target'),
      id: infraction?.id,
      missionId,
      actionId,
      controlId,
      controlType
    }
    await mutate({ variables: { infraction: mutationData } })
    setShowInfractionForm(false)
  }
  const onDelete = async (data: Infraction) => {
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
            <form onSubmit={(e: React.FormEvent) => onSubmit(e, formData)}>
              <InfractionForm
                infraction={formData}
                onChange={onChangeFormField}
                onCancel={() => setShowInfractionForm(false)}
              />
            </form>
          </div>
        </>
      )}
    </>
  )
}

export default ControlInfraction
