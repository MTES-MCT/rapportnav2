import Text, { TextProps } from '@common/components/ui/text.tsx'
import { FC, ReactNode } from 'react'
import { Tooltip, Whisper, WhisperProps } from 'rsuite'

type HoverTooltipProps = Pick<WhisperProps, 'placement'> &
  Partial<Omit<TextProps, 'children' | 'truncate'>> & {
    text?: string
    children?: ReactNode
  }

const HoverTooltip: FC<HoverTooltipProps> = ({ text, children, placement = 'top', as = 'h3', ...textProps }) => (
  <Whisper placement={placement} trigger="hover" speaker={<Tooltip>{text}</Tooltip>}>
    <span>
      {children ?? (
        <Text as={as} truncate {...textProps}>
          {text}
        </Text>
      )}
    </span>
  </Whisper>
)

export default HoverTooltip
