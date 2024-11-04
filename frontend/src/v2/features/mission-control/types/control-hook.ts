import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'

export interface AbstractControlFormikHook<M> extends AbstractFormikSubFormHook<M> {
  controlTypeLabel: string | undefined
  radios?: { name: string; label: string; extra?: boolean }[]
}
