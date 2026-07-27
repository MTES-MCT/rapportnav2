import { isEqual } from 'lodash'
import { useEffect, useState } from 'react'
import { AbstractFormikHook } from '../types/abstract-formik-hook'

const isPlainObject = (value: unknown): value is Record<string, unknown> =>
  typeof value === 'object' && value !== null && value.constructor === Object

//TODO: apply to every input of abstrct-formik
export const normalizeNulls = <V,>(value: V): V => {
  if (value === null) return undefined as unknown as V
  if (Array.isArray(value)) return value.map(normalizeNulls) as unknown as V
  if (isPlainObject(value)) {
    return Object.fromEntries(Object.entries(value).map(([k, v]) => [k, normalizeNulls(v)])) as V
  }
  return value
}

export const normalizeBooleans = <V,>(value: V, keys: string[]): V => {
  if (Array.isArray(value)) return value.map(v => normalizeBooleans(v, keys)) as unknown as V
  if (isPlainObject(value)) {
    return Object.fromEntries(
      Object.entries(value).map(([k, v]) => [k, keys.includes(k) ? !!v : normalizeBooleans(v, keys)])
    ) as V
  }
  return value
}

const isEmptyShell = (value: unknown): boolean => {
  if (value === undefined || value === false) return true
  if (Array.isArray(value)) return value.length === 0
  if (isPlainObject(value)) return Object.values(value).every(isEmptyShell)
  return false
}

export const pruneEmptyShells = <V,>(value: V): V => {
  if (Array.isArray(value)) return value.map(pruneEmptyShells) as unknown as V
  if (isPlainObject(value)) {
    const entries = Object.entries(value)
      .map(([k, v]) => [k, pruneEmptyShells(v)] as const)
      .filter(([, v]) => v !== undefined)
    const pruned = Object.fromEntries(entries)
    return (isEmptyShell(pruned) ? undefined : pruned) as V
  }
  return value
}

export function useAbstractFormik<T, M>(
  value: T,
  fromFieldValueToInput: (value: T) => M,
  fromInputToFieldValue: (input: M) => T,
  booleans?: string[]
): AbstractFormikHook<T, M> {
  const [initValue, setInitValue] = useState<M>()

  const beforeInitValue = (value?: T): M | undefined => {
    let fieldValue = { ...normalizeNulls(value) } as T
    if (booleans) fieldValue = normalizeBooleans(fieldValue, booleans)
    return fromFieldValueToInput(fieldValue)
  }

  useEffect(() => {
    const valueToInit = beforeInitValue(value)
    setInitValue(valueToInit)

    return () => {
      setInitValue(undefined)
    }
  }, [value])

  const normalizeValue = (value?: M): M | undefined => {
    const withoutNulls = normalizeNulls(value)
    const withBooleans = booleans ? normalizeBooleans(withoutNulls, booleans) : withoutNulls
    return pruneEmptyShells(withBooleans)
  }

  const beforeSubmit = (value?: M): T | undefined => {
    if (!value) return
    if (isEqual(normalizeValue(value), normalizeValue(initValue))) return
    return fromInputToFieldValue(pruneEmptyShells(value))
  }

  const handleSubmit = async (value?: M, onSubmit?: (valueToSubmit: T) => Promise<unknown>) => {
    const valueToSubmit = beforeSubmit(value)

    if (onSubmit && valueToSubmit) {
      setInitValue(value)
      await onSubmit(valueToSubmit)
    }
  }

  return {
    initValue,
    handleSubmit,
    beforeSubmit,
    beforeInitValue
  }
}
