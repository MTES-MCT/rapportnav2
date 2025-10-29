import { IconProps } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { FunctionComponent } from 'react'

export interface AdminAction {
  label?: string
  color?: string
  formProps?: any
  isMain?: boolean
  disabled?: boolean
  key: AdminActionType
  icon?: FunctionComponent<IconProps>
  form: FunctionComponent<{ formik: FormikProps<unknown> } & any>
}

export enum AdminActionType {
  DELETE = 'DELETE',
  UPDATE = 'UPDATE',
  CREATE = 'CREATE',
  UPDATE_PASSWORD = 'UPDATE_PASSWORD',
  ROTATE_KEY = 'ROTATE_KEY'
}
