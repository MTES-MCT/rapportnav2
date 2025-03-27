import { Icon, Tag, THEME } from '@mtes-mct/monitor-ui'
import { useOnlineManager } from '../../hooks/use-online-manager.tsx'

const OfflineTag = () => {
  const { isOnline } = useOnlineManager()

  return isOnline ? null : (
    <Tag
      backgroundColor={THEME.color.maximumRed15}
      color={THEME.color.charcoal}
      Icon={Icon.Reject}
      withCircleIcon={true}
    >
      Hors ligne
    </Tag>
  )
}

export default OfflineTag
