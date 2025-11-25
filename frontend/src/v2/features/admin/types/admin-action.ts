import { IconProps } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FunctionComponent } from 'react'

export interface AdminAction {
  title?: string
  label?: string
  color?: string
  formProps?: any
  isMain?: boolean
  key: AdminActionType
  icon?: FunctionComponent<IconProps>
  disabled?: (rowData: unknown) => boolean
  form: FunctionComponent<{ formik: FormikProps<unknown> } & any>
}

export enum AdminActionType {
  DELETE = 'DELETE',
  UPDATE = 'UPDATE',
  CREATE = 'CREATE',
  CREATE_USER = 'CREATE_USER',
  DISABLE_AGENT = 'DISABLE_AGENT',
  MIGRATE_AGENT = 'MIGRATE_AGENT',
  UPDATE_PASSWORD = 'UPDATE_PASSWORD',
  ROTATE_KEY = 'ROTATE_KEY'
}
