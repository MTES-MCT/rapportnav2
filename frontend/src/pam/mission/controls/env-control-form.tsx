import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity,
  ControlType
} from '../../../types/control-types'
import { NumberInput, TextInput } from '@mtes-mct/monitor-ui'
import omit from 'lodash/omit'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from './control-title-checkbox'
import useAddOrUpdateControl from './use-add-update-control.tsx'
import useDeleteControl from './use-delete-control.tsx'

export interface EnvControlFormProps {
  controlType: ControlType
  data?: ControlAdministrative | ControlSecurity | ControlNavigation | ControlGensDeMer
  maxAmountOfControls?: number
  shouldCompleteControl?: boolean
}

const EnvControlForm: FC<EnvControlFormProps> = ({ controlType, data, maxAmountOfControls, shouldCompleteControl }) => {
  const { missionId, actionId } = useParams()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(data?.observations)

  const handleObservationsChange = (nextValue?: string) => {
    setObservationsValue(nextValue)
  }

  useEffect(() => {
    setObservationsValue(data?.observations)
  }, [data])

  const handleObservationsBlur = async () => {
    await onChange('observations', observationsValue)
  }

  const [mutateControl] = useAddOrUpdateControl({ controlType: controlType })
  const [deleteControl] = useDeleteControl({ controlType: controlType })

  const toggleControl = async (isChecked: boolean) =>
    isChecked
      ? onChange()
      : await deleteControl({
          variables: {
            actionId
          }
        })

  const onChange = async (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename'),
      missionId: missionId,
      actionControlId: actionId,
      amountOfControls: data?.amountOfControls
    }

    // do not create a control just because of a blur on observations
    if (field === 'observations') {
      if (observationsValue === undefined && value === undefined) {
        return
      }
    }

    if (!!field && !!value) {
      updatedData = {
        ...updatedData,
        [field]: value
      }
    }

    if (field === 'amountOfControls' && (value === 0 || value === undefined)) {
      await deleteControl({
        variables: {
          actionId
        }
      })
    } else {
      await mutateControl({ variables: { control: updatedData } })
    }
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <ControlTitleCheckbox
          controlType={controlType}
          checked={!!data || shouldCompleteControl}
          // disabled={mutationLoading?.loading}
          shouldCompleteControl={!!shouldCompleteControl && !data}
          onChange={(isChecked: boolean) => toggleControl(isChecked)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" style={{ width: '100%' }} spacing={'0.5rem'}>
          <Stack.Item style={{ width: '33%' }}>
            <NumberInput
              label="Nb contrôles"
              isRequired={shouldCompleteControl}
              name="amountOfControls"
              // disabled={mutationLoading?.loading}
              value={data?.amountOfControls}
              // max={maxAmountOfControls}
              isLight={true}
              onChange={(nextValue?: number) => onChange('amountOfControls', nextValue)}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '67%' }}>
            <TextInput
              label="Observations (hors infraction)"
              name="observations"
              isLight={true}
              // disabled={mutationLoading?.loading}
              value={observationsValue}
              onChange={handleObservationsChange}
              onBlur={handleObservationsBlur}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default EnvControlForm
