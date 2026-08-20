import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FlexboxGrid } from 'rsuite'

type BasicDialogTitleProps = {
  label?: string
  onClose: () => void
}

const BasicDialogTitle: React.FC<BasicDialogTitleProps> = ({ label, onClose }) => {
  return (
    <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 14, paddingRight: 24, width: '100%' }}>
      <FlexboxGrid.Item style={{ fontSize: '16px' }}>{label}</FlexboxGrid.Item>
      <FlexboxGrid.Item>
        <IconButton
          Icon={Icon.Close}
          size={Size.NORMAL}
          accent={Accent.TERTIARY}
          color={THEME.color.gainsboro}
          role={'dialog-form'}
          data-testid="close-dialog-form"
          onClick={onClose}
        />
      </FlexboxGrid.Item>
    </FlexboxGrid>
  )
}

export default BasicDialogTitle
