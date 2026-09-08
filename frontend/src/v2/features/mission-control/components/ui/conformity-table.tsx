import { ControlCheck } from '@common/types/fish-mission-types.ts'
import { Label, Radio, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'

export const CenteredRadio = styled(Radio)({
  '&&& .rs-radio-control': {
    position: 'static',
    top: 'auto'
  },
  '&&& .rs-radio-checker': {
    padding: 0
  },
  '&&& .rs-radio-inner::before': {
    backgroundColor: 'transparent',
    border: `2px solid ${THEME.color.lightGray} !important`
  },
  '&&& .rs-radio-inner::after': {
    backgroundColor: THEME.color.charcoal
  }
})

export interface ConformityRow<Field extends string> {
  field: Field
  label: string
  hide?: (values: Partial<Record<Field, ControlCheck | undefined>>) => boolean
}

export interface ConformitySection<Field extends string> {
  title?: string
  rows: ConformityRow<Field>[]
}

const CONFORMITY_COLUMNS: { value: ControlCheck; header: string }[] = [
  { value: ControlCheck.YES, header: 'Oui' },
  { value: ControlCheck.NO, header: 'Non' },
  { value: ControlCheck.NOT_APPLICABLE, header: 'N/A' }
]

interface ConformityTableProps<Field extends string> {
  rows: ConformitySection<Field>[]
  values: Partial<Record<Field, ControlCheck | undefined>>
}

export function ConformityTable<Field extends string>({ rows, values }: ConformityTableProps<Field>) {
  const renderTitle = (section: ConformitySection<Field>, index: number) => (
    <>
      {index > 0 && <div style={{ borderTop: `1px solid ${THEME.color.lightGray}`, margin: '0.5rem 0' }} />}
      <Stack.Item style={{ padding: '0.5rem 1rem 0' }}>
        <Label style={{ margin: 0 }}>{section.title}</Label>
      </Stack.Item>
    </>
  )

  const renderRow = (row: ConformityRow<Field>) => {
    if (row.hide && row.hide(values)) return null

    return (
      <React.Fragment key={row.field}>
        <Stack direction="row" alignItems="center">
          <Stack.Item style={{ flex: 1, padding: '0.5rem 1rem' }}>
            <Label $isRequired style={{ margin: 0, color: THEME.color.gunMetal }}>
              {row.label}
            </Label>
          </Stack.Item>
          {CONFORMITY_COLUMNS.map(column => (
            <Stack.Item key={column.value} style={{ width: '5rem', display: 'flex', justifyContent: 'center' }}>
              <CenteredRadio
                readOnly
                name={row.field}
                value={column.value}
                labelPosition="right"
                onChange={function noRefCheck() {}}
                checked={values?.[row.field] === column.value}
              />
            </Stack.Item>
          ))}
        </Stack>
      </React.Fragment>
    )
  }

  return (
    <>
      <Stack direction="row" alignItems="center">
        <Stack.Item style={{ flex: 1 }} />
        {CONFORMITY_COLUMNS.map(column => (
          <Stack.Item key={column.value} style={{ width: '5rem', padding: '0.75rem 0.5rem', textAlign: 'center' }}>
            <Label style={{ margin: 0, color: THEME.color.gunMetal }}>{column.header}</Label>
          </Stack.Item>
        ))}
      </Stack>
      <Stack.Item style={{ width: '100%' }}>
        <div style={{ borderTop: `1px solid ${THEME.color.lightGray}` }} />
      </Stack.Item>

      {rows.map((section, index) => {
        const visibleRows = section.rows.filter(row => !(row.hide && row.hide(values)))
        if (visibleRows.length === 0) return null

        return (
          <React.Fragment key={section.title ?? `section-${index}`}>
            {section.title && renderTitle(section, index)}
            {visibleRows.map(row => renderRow(row))}
          </React.Fragment>
        )
      })}
    </>
  )
}
