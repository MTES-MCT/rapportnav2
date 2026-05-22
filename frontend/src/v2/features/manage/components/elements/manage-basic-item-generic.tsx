import { Accent, Button } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import AdminSectionWrapper from '../../../common/components/layout/admin-section-wrapper'
import BasicDialogForm from '../../../common/components/ui/basic-dialog-form'
import BasicItemTable from '../../../common/components/ui/basic-item-table'
import useAdminBasicItem from '../../../common/hooks/use-table-basic-item'
import { AdminActionType, BasicAction, BasicCell } from '../../../common/types/basic-action'

type ManageBasicItemGenericProps = {
  data?: any[]
  defaultData?: any
  cells: BasicCell[]
  onSubmit: (action: AdminActionType, value: any) => Promise<void>
  mainAction?: BasicAction
  actions: BasicAction[]
}

const ManageBasicItemGeneric: React.FC<ManageBasicItemGenericProps> = ({
  data,
  cells,
  onSubmit,
  mainAction,
  actions
}) => {
  const { currentData, showDialogForm, currentAction, handleAction, handleSubmit } = useAdminBasicItem({ onSubmit })

  return (
    <AdminSectionWrapper>
      <Stack direction="column" justifyContent="flex-start" alignItems="flex-start">
        <Stack.Item style={{ width: '100%', display: 'flex', justifyContent: 'flex-end' }}>
          {mainAction && (
            <Button Icon={mainAction.icon} accent={Accent.PRIMARY} onClick={() => handleAction(mainAction, {})}>
              {mainAction.label}
            </Button>
          )}
        </Stack.Item>
        <Stack.Item style={{ width: '100%', marginTop: 2 }}>
          <BasicItemTable data={data} cells={cells} actions={actions} onAction={handleAction} />
        </Stack.Item>

        {showDialogForm && currentAction && (
          <BasicDialogForm
            action={currentAction}
            initValue={currentData ?? {}}
            onSubmit={(response, value) => handleSubmit(response, value)}
          />
        )}
      </Stack>
    </AdminSectionWrapper>
  )
}

export default ManageBasicItemGeneric
