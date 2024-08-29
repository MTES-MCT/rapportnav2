import { ControlType } from '@common/types/control-types'
import { useEffect, useRef, useState } from 'react'
import useAddOrUpdateControl from '../use-add-update-control'
import useDeleteControl from '../use-delete-control'

const DEBOUNCE_TIME_TRIGGER = 5000

type ControlHook = {
  isRequired?: boolean
  controlIsChecked?: boolean
  updateControl: (actionId: string, value?: unknown) => Promise<void>
  controlChanged: (actionId?: string, control?: unknown) => Promise<void>
  toggleControl: (isChecked: boolean, actionId?: string, control?: unknown) => Promise<void>
}

export function useControl(data: unknown, controlType: ControlType, shouldCompleteControl?: boolean): ControlHook {
  const [isRequired, setIsRequired] = useState<boolean>()
  const timerRef = useRef<ReturnType<typeof setTimeout>>()
  const [controlIsChecked, setControlIsChecked] = useState<boolean>()

  const [deleteControl] = useDeleteControl({ controlType })
  const [mutateControl] = useAddOrUpdateControl({ controlType })

  useEffect(() => {
    setIsRequired(!!shouldCompleteControl && !!!data)
    setControlIsChecked(!!data || shouldCompleteControl)
  }, [data, shouldCompleteControl])

  const controlChanged = async (actionId?: string, control?: unknown): Promise<void> => {
    clearTimeout(timerRef.current)
    timerRef.current = setTimeout(() => updateControl(actionId, control), DEBOUNCE_TIME_TRIGGER)
  }

  const updateControl = async (actionId?: string, control?: unknown) => {
    if (!control) return
    let variables = {
      control: { ...control, actionControlId: actionId }
    }
    await mutateControl({ variables })
  }

  const toggleControl = async (isChecked: boolean, actionId?: string, control?: unknown) => {
    setControlIsChecked(isChecked)
    if (!isChecked) {
      await deleteControl({ variables: { actionId } })
    } else {
      await updateControl(actionId, control)
    }
  }

  return {
    isRequired,
    updateControl,
    toggleControl,
    controlChanged,
    controlIsChecked
  }
}
