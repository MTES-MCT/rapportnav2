import { Accent, IconButton, Size } from '@mtes-mct/monitor-ui'
import { get } from 'lodash'
import React, { Fragment } from 'react'
import { Table } from 'rsuite'
import { Cell, HeaderCell, RowDataType } from 'rsuite-table'
import Column from 'rsuite/esm/Table/TableColumn'
import { useDate } from '../../hooks/use-date'
import { BasicAction, BasicCell } from '../../types/basic-action'

const DATE_LABELS = ['updatedAt', 'createdAt', 'deletedAt', 'disabledAt']

type BasicItemTableProps = {
  data?: any[]
  cells: BasicCell[]
  actions: BasicAction[]
  onAction: (action: BasicAction, rowData: RowDataType<any>) => void
}

const BasicItemTable: React.FC<BasicItemTableProps> = ({ data, cells, actions, onAction }) => {
  const { formatDateTimeForFrenchHumans } = useDate()

  return (
    <Table height={650} data={data}>
      {cells.map(cell => (
        <Column width={cell.width} align="start" fixed key={cell.key}>
          <HeaderCell>{cell.label}</HeaderCell>
          <Cell>
            {rowData => {
              const value = get(rowData, cell.key) as any
              const isDateTime = DATE_LABELS.includes(cell.key) || cell.type?.format === 'datetime'
              return isDateTime
                ? formatDateTimeForFrenchHumans(value)
                : value !== null && value !== undefined
                  ? `${value}`
                  : ''
            }}
          </Cell>
        </Column>
      ))}
      <Column width={actions.length > 3 ? 200 : 100} align="end" fixed="right">
        <HeaderCell>Actions</HeaderCell>
        <Cell>
          {rowData => (
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              {actions.map((rowAction, index) => (
                <Fragment key={`${rowAction.key}-${index}`}>
                  {rowAction.icon && (
                    <IconButton
                      title={rowAction.title}
                      size={Size.NORMAL}
                      Icon={rowAction.icon}
                      color={rowAction.color}
                      accent={Accent.TERTIARY}
                      role={`action-cell-${index}`}
                      onClick={() => onAction(rowAction, rowData)}
                      disabled={rowAction.disabled ? rowAction.disabled(rowData) : false}
                    />
                  )}
                </Fragment>
              ))}
            </div>
          )}
        </Cell>
      </Column>
    </Table>
  )
}

export default BasicItemTable
