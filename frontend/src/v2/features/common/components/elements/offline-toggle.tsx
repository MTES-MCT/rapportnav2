import { FC } from 'react'
import { Toggle, ToggleProps } from '@mtes-mct/monitor-ui'
import { useOnlineManager } from '../../hooks/use-online-manager.tsx'

export type OfflineToggleProps = {} & Omit<ToggleProps, 'label' | 'name'>

export const OfflineToggle: FC<OfflineToggleProps> = ({}) => {
  const { isOnline, setOnline } = useOnlineManager()
  return (
    <Toggle //
      label={'online toggle'}
      name={'online-toggle'}
      checked={isOnline}
      onChange={(isChecked: boolean) => {
        setOnline(isOnline && isChecked)
      }}
    />
  )
}

export default OfflineToggle
