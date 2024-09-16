import { ControlType } from '@common/types/control-types'
import { isBoolean } from 'lodash'
import { useEffect, useRef, useState } from 'react'
import useAddOrUpdateControl from '../use-add-update-control'
import useDeleteControl from '../use-delete-control'

const DEBOUNCE_TIME_TRIGGER = 5000

type Control = unknown & { unitHasConfirmed?: boolean }
type ControlHook = {
  isRequired?: boolean
  controlIsChecked?: boolean
  updateControl: (actionId: string, value?: Control) => Promise<void>
  controlChanged: (actionId?: string, control?: Control) => Promise<void>
  toggleControl: (isChecked: boolean, actionId?: string, control?: Control) => Promise<void>
  controlEnvChanged: (actionId?: string, control?: Control, shouldUpdate?: boolean) => Promise<void>
}

export function useControl(
  data: unknown,
  controlType: ControlType,
  shouldCompleteControl?: boolean,
  debounceTime?: number
): ControlHook {
  const [isRequired, setIsRequired] = useState<boolean>()
  const timerRef = useRef<ReturnType<typeof setTimeout>>()
  const [controlIsChecked, setControlIsChecked] = useState<boolean>()

  const [deleteControl] = useDeleteControl({ controlType })
  const [mutateControl] = useAddOrUpdateControl({ controlType })

  useEffect(() => {
    setIsRequired(!!shouldCompleteControl && !!!data)
    setControlIsChecked(!!data || shouldCompleteControl)
  }, [data, shouldCompleteControl])

  const controlChanged = async (actionId?: string, control?: Control): Promise<void> => {
    clearTimeout(timerRef.current)
    timerRef.current = setTimeout(() => updateControl(actionId, control), debounceTime || DEBOUNCE_TIME_TRIGGER)
  }

  const updateControl = async (actionId?: string, control?: Control) => {
    if (!control) return
    if (!isBoolean(control.unitHasConfirmed)) control.unitHasConfirmed = true
    let variables = {
      control: { ...control, actionControlId: actionId }
    }
    await mutateControl({ variables })
  }

  const toggleControl = async (isChecked: boolean, actionId?: string, control?: Control) => {
    if (isChecked === controlIsChecked) return
    setControlIsChecked(isChecked)
    updateOrDeleteControl(actionId, control, isChecked)
  }

  const updateOrDeleteControl = async (actionId?: string, control?: Control, shouldUpdate?: boolean) => {
    shouldUpdate ? await updateControl(actionId, control) : await deleteControl({ variables: { actionId } })
  }

  const controlEnvChanged = async (actionId?: string, control?: Control, shouldUpdate?: boolean): Promise<void> => {
    clearTimeout(timerRef.current)
    timerRef.current = setTimeout(() => updateOrDeleteControl(actionId, control, shouldUpdate), DEBOUNCE_TIME_TRIGGER)
  }

  return {
    isRequired,
    updateControl,
    toggleControl,
    controlChanged,
    controlIsChecked,
    controlEnvChanged
  }
}
