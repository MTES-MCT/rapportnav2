import Text from '@common/components/ui/text'
import { Accent, Button, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import React, { FunctionComponent, useState } from 'react'
import { Stack, Table } from 'rsuite'
import { Cell, HeaderCell, RowDataType } from 'rsuite-table'
import Column from 'rsuite/esm/Table/TableColumn'
import AdminSectionWrapper from '../../../common/components/layout/admin-section-wrapper'
import DialogQuestion from '../../../common/components/ui/dialog-question'
import { useDate } from '../../../common/hooks/use-date'
import { AdminAction, AdminCell, AdminService } from '../../types/admin-services-type'
import DialogForm from '../ui/dialog-form'

type AdminServiceProps = {
  data?: any[]
  module: string
  cells: AdminCell[]
  onSubmit: (action: AdminAction, value: any) => Promise<void>
  form: FunctionComponent<{ formik: FormikProps<unknown> }>
}

const AdminBasicItemGeneric: React.FC<AdminServiceProps> = ({ form, data, cells, module, onSubmit }) => {
  const { formatDateTimeForFrenchHumans } = useDate()
  const [action, setAction] = useState<AdminAction>()
  const [currentData, setCurrentData] = useState<any>()
  const [showDialogForm, setShowDialogForm] = useState(false)
  const [showDialogQuestion, setShowDialogQuestion] = useState(false)

  const handleAction = (action: AdminAction, rowData: RowDataType<any>) => {
    setAction(action)
    setCurrentData(rowData)
    setShowDialogForm(action !== 'DELETE')
    setShowDialogQuestion(action === 'DELETE')
  }

  const handleSubmit = async (response: boolean, value: AdminService) => {
    setShowDialogForm(false)
    setShowDialogQuestion(false)
    setCurrentData(undefined)
    if (!response) return
    if (action) onSubmit(action, value)
    setAction(undefined)
  }

  return (
    <AdminSectionWrapper>
      <Stack direction="column" justifyContent="flex-start" alignItems="flex-start" spacing={'2rem'}>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" justifyContent="space-between" alignItems="flex-start">
            <Stack.Item>
              <Text as="h1">{`All ${module} (${data?.length})`}</Text>
            </Stack.Item>
            <Stack.Item>
              <Button accent={Accent.PRIMARY} onClick={() => handleAction('CREATE', {})}>
                {` Créer un ${module}`}
              </Button>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Table height={550} data={data} onRowClick={rowData => console.log(rowData)}>
            {cells.map(cell => (
              <Column width={cell.width} align="start" fixed>
                <HeaderCell>{cell.label}</HeaderCell>
                <Cell>
                  {rowData =>
                    !['updatedAt', 'createdAt', 'deletedAt'].includes(cell.key)
                      ? rowData[cell.key]
                      : formatDateTimeForFrenchHumans(rowData[cell.key])
                  }
                </Cell>
              </Column>
            ))}
            <Column width={150} fixed="right">
              <HeaderCell>Actions</HeaderCell>

              <Cell style={{ padding: '6px' }}>
                {rowData => (
                  <Stack direction="row" spacing={'.5rem'}>
                    <Stack.Item>
                      <IconButton
                        role="edit-cell"
                        Icon={Icon.Edit}
                        size={Size.NORMAL}
                        accent={Accent.TERTIARY}
                        onClick={() => handleAction('UPDATE', rowData)}
                      />
                    </Stack.Item>
                    <Stack.Item>
                      <IconButton
                        disabled={true}
                        role="edit-delete"
                        Icon={Icon.Delete}
                        size={Size.NORMAL}
                        accent={Accent.TERTIARY}
                        color={THEME.color.maximumRed}
                        onClick={() => handleAction('DELETE', rowData)}
                      />
                    </Stack.Item>
                  </Stack>
                )}
              </Cell>
            </Column>
          </Table>
        </Stack.Item>
        {showDialogForm && (
          <DialogForm
            component={form}
            initValue={currentData ?? {}}
            onSubmit={(response, value) => handleSubmit(response, value)}
            title={`${currentData?.id ? 'Mise à jour' : 'Nouveau'} ${module}`}
          />
        )}
        {showDialogQuestion && (
          <DialogQuestion
            type="danger"
            title={`Suppression de ${module}`}
            question={`Voulez vous vraiment supprimer ce (cette) ${module}?`}
            onSubmit={response => handleSubmit(response, currentData)}
          />
        )}
      </Stack>
    </AdminSectionWrapper>
  )
}

export default AdminBasicItemGeneric
