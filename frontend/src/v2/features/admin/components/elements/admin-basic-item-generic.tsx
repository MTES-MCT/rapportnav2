import Text from '@common/components/ui/text'
import { Accent, Button, IconButton, Size } from '@mtes-mct/monitor-ui'
import React, { useState } from 'react'
import { Stack, Table } from 'rsuite'
import { Cell, HeaderCell, RowDataType } from 'rsuite-table'
import Column from 'rsuite/esm/Table/TableColumn'
import AdminSectionWrapper from '../../../common/components/layout/admin-section-wrapper'
import { useDate } from '../../../common/hooks/use-date'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminCell } from '../../types/admin-services-type'
import DialogForm from '../ui/dialog-form'
import { get } from 'lodash'

const DATE_LABELS = ['updatedAt', 'createdAt', 'deletedAt', 'disabledAt']

type AdminServiceProps = {
  data?: any[]
  title: string
  defaultData?: any
  cells: AdminCell[]
  onSubmit: (action: AdminActionType, value: any) => Promise<void>
  actions: AdminAction[]
}

const AdminBasicItemGeneric: React.FC<AdminServiceProps> = ({ data, defaultData, cells, title, onSubmit, actions }) => {
  const { formatDateTimeForFrenchHumans } = useDate()
  const [currentData, setCurrentData] = useState<any>()
  const [actionIndex, setActionIndex] = useState<number>()
  const [showDialogForm, setShowDialogForm] = useState(false)

  const handleAction = (index: number, rowData: RowDataType<any>) => {
    setCurrentData(rowData)
    setActionIndex(index)
    setShowDialogForm(true)
  }

  const handleSubmit = async (response: boolean, value: any) => {
    setShowDialogForm(false)
    setCurrentData(undefined)
    if (!response) return
    if (actionIndex !== undefined) onSubmit(actions[actionIndex].key, value)
    setActionIndex(undefined)
  }

  return (
    <AdminSectionWrapper>
      <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={'2rem'}>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" justifyContent="space-between" alignItems="flex-start">
            <Stack.Item>
              <Text as="h1">{`${title} (${data?.length ?? 0})`}</Text>
            </Stack.Item>
            <Stack.Item>
              {(() => {
                const index = actions.findIndex(action => action.isMain)
                if (index === -1) return <></>
                return (
                  <Button accent={Accent.PRIMARY} onClick={() => handleAction(index, defaultData ?? {})}>
                    {actions[index].label}
                  </Button>
                )
              })()}
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Table height={650} data={data}>
            {cells.map(cell => (
              <Column width={cell.width} align="start" fixed>
                <HeaderCell>{cell.label}</HeaderCell>
                <Cell>
                  {rowData => {
                    const data = get(rowData, cell.key) as any
                    return !DATE_LABELS.includes(cell.key) ? data : formatDateTimeForFrenchHumans(data)
                  }}
                </Cell>
              </Column>
            ))}
            <Column width={200} fixed="right">
              <HeaderCell>Actions</HeaderCell>

              <Cell style={{ padding: 2 }}>
                {rowData => (
                  <Stack direction="row" spacing={'.5rem'}>
                    {actions.map((rowAction, index) => (
                      <Stack.Item>
                        {rowAction.icon && (
                          <IconButton
                            title={rowAction.title}
                            size={Size.NORMAL}
                            Icon={rowAction.icon}
                            color={rowAction.color}
                            accent={Accent.TERTIARY}
                            role={`action-cell-${index}`}
                            onClick={() => handleAction(index, rowData)}
                            disabled={rowAction.disabled ? rowAction.disabled(rowData) : false}
                          />
                        )}
                      </Stack.Item>
                    ))}
                  </Stack>
                )}
              </Cell>
            </Column>
          </Table>
        </Stack.Item>
        {showDialogForm && actionIndex !== undefined && (
          <DialogForm
            action={actions[actionIndex]}
            initValue={currentData ?? {}}
            onSubmit={(response, value) => handleSubmit(response, value)}
          />
        )}
      </Stack>
    </AdminSectionWrapper>
  )
}

export default AdminBasicItemGeneric
