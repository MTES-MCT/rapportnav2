import React, { useMemo } from 'react'
import { Stack } from 'rsuite'
import AdminSectionWrapper from '../../../common/components/layout/admin-section-wrapper'
import DialogForm from '../../../common/components/ui/basic-dialog-form'
import AdminBasicItemTable from '../../../common/components/ui/basic-item-table'
import useAdminBasicItem from '../../../common/hooks/use-table-basic-item'
import { AdminActionType, BasicAction, BasicCell } from '../../../common/types/basic-action'
import AdminBasicItemHeader from '../ui/admin-basic-item-header'

type AdminServiceProps = {
  data?: any[]
  title: string
  defaultData?: any
  cells: BasicCell[]
  onSubmit: (action: AdminActionType, value: any) => Promise<void>
  actions: BasicAction[]
}

const AdminBasicItemGeneric: React.FC<AdminServiceProps> = ({ data, defaultData, cells, title, onSubmit, actions }) => {
  const mainAction = useMemo(() => actions.find(a => a.isMain), [actions])
  const listActions = useMemo(() => actions.filter(a => !a.isMain), [actions])
  const { currentData, showDialogForm, currentAction, handleAction, handleSubmit } = useAdminBasicItem({ onSubmit })

  return (
    <AdminSectionWrapper>
      <Stack direction="column" justifyContent="center" alignItems="center" spacing={'2rem'}>
        <Stack.Item style={{ width: '100%' }}>
          <AdminBasicItemHeader
            title={title}
            count={data?.length}
            mainAction={mainAction}
            onAction={action => handleAction(action, defaultData ?? {})}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <AdminBasicItemTable data={data} cells={cells} actions={listActions} onAction={handleAction} />
        </Stack.Item>
        {showDialogForm && currentAction && (
          <DialogForm
            action={currentAction}
            initValue={currentData ?? {}}
            onSubmit={(response, value) => handleSubmit(response, value)}
          />
        )}
      </Stack>
    </AdminSectionWrapper>
  )
}

export default AdminBasicItemGeneric
