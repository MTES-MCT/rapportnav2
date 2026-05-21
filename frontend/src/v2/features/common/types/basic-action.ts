import { Accent, IconProps } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FunctionComponent } from 'react'

export type CellType = {
  format?: string
  accent?: Accent
}

export type BasicCell = { key: string; label: string; width: number; type?: CellType }

export interface BasicAction {
  title?: string
  label?: string
  color?: string
  formProps?: any
  isMain?: boolean
  accent?: Accent
  key: AdminActionType
  validateButton?: string
  icon?: FunctionComponent<IconProps>
  disabled?: (rowData: unknown) => boolean
  form: FunctionComponent<{ formik: FormikProps<unknown>; [key: string]: any }>
}

export enum AdminActionType {
  DELETE = 'DELETE',
  UPDATE = 'UPDATE',
  CREATE = 'CREATE',
  CREATE_USER = 'CREATE_USER',
  DISABLE_USER = 'DISABLE_USER',
  DISABLE_AGENT = 'DISABLE_AGENT',
  MIGRATE_AGENT = 'MIGRATE_AGENT',
  UPDATE_PASSWORD = 'UPDATE_PASSWORD',
  ROTATE_KEY = 'ROTATE_KEY',
  MANAGE_CREATE = 'MANAGE_CREATE',
  MANAGE_UPDATE = 'MANAGE_UPDATE',
  MANAGE_DELETE = 'MANAGE_DELETE'
}
