import { useEffect, useState } from 'react'
import { useDelay } from './use-delay'

type DelayHook = {
  value?: string | number
  onChange: (v?: string | number) => void
}

export function useDelayFormik(
  initValue: string | number | undefined,
  onSubmit: (value?: string | number) => void | Promise<any>,
  delay?: number
): DelayHook {
  const { handleExecuteOnDelay } = useDelay()
  const [value, setValue] = useState<string | number>()

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- syncs parent-controlled prop to local editable state
    if (initValue) setValue(initValue)
  }, [initValue])

  const onChange = (v?: string | number) => {
    setValue(v)
    handleExecuteOnDelay(async () => {
      onSubmit(v)
    }, delay ?? 3000)
  }

  return {
    value,
    onChange
  }
}
