import { ControlAdministrative, ControlType } from '@common/types/control-types'
import { FieldProps } from 'formik'
import { AbstractControlFormikHook } from '../types/control-hook'
import { useAbstractControl } from './use-abstract-control'
import { useControlRegistry } from './use-control-registry'

const emptyAdministrative = {
  observations: undefined,
  compliantOperatingPermit: undefined,
  upToDateNavigationPermit: undefined,
  compliantSecurityDocuments: undefined,
  infractions: []
}

const radios = [
  { name: 'compliantOperatingPermit', label: 'Permis de mise en exploitation (autorisation à pêcher) conforme' },
  { name: 'upToDateNavigationPermit', label: 'Permis de navigation à jour' },
  { name: 'compliantSecurityDocuments', label: 'Titres de sécurité conformes' }
]

export type ControlAdministrativeInput = {} & ControlAdministrative

export function useControlAdministrative(
  name: string,
  fieldFormik: FieldProps<ControlAdministrative>
): AbstractControlFormikHook<ControlAdministrativeInput> {
  const { getControlType } = useControlRegistry()
  const { initValue, handleSubmit } = useAbstractControl<ControlAdministrative, ControlAdministrativeInput>(
    name,
    fieldFormik,
    (input: ControlAdministrative) => input as ControlAdministrativeInput,
    (value?: ControlAdministrativeInput) => {
      return (
        !value ? value : { ...value, ...(!value?.hasBeenDone ? emptyAdministrative : {}) }
      ) as ControlAdministrative
    }
  )

  return {
    radios,
    initValue,
    handleSubmit,
    controlTypeLabel: getControlType(ControlType.ADMINISTRATIVE)
  }
}
