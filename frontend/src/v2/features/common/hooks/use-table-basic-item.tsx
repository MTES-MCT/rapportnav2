import { useState } from 'react'
import { RowDataType } from 'rsuite-table'
import { AdminActionType, BasicAction } from '../types/basic-action'

type UseBasicTableItemProps = {
  onSubmit: (action: AdminActionType, value: any) => Promise<void>
}

const useBasicTableItem = ({ onSubmit }: UseBasicTableItemProps) => {
  const [currentData, setCurrentData] = useState<any>()
  const [showDialogForm, setShowDialogForm] = useState(false)
  const [currentAction, setCurrentAction] = useState<BasicAction>()

  const handleAction = (action: BasicAction, rowData: RowDataType<any>) => {
    setCurrentData(rowData)
    setCurrentAction(action)
    setShowDialogForm(true)
  }

  const handleSubmit = async (response: boolean, value: any) => {
    setShowDialogForm(false)
    setCurrentData(undefined)
    if (!response) return
    if (currentAction !== undefined) onSubmit(currentAction.key, value)
    setCurrentAction(undefined)
  }

  return { currentData, showDialogForm, currentAction, handleAction, handleSubmit }
}

export default useBasicTableItem
