import { Message } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

const JPE_AVAILABLE = `L’échelle de coupée était mise à disposition et praticable `
const JPE_UNAVAILABLE = `L’échelle de coupée n’a pas été mise à disposition / n’était pas praticable`

interface JpeMessageProps {
  jpe?: boolean
}

const JpeMessage: FC<JpeMessageProps> = ({ jpe }) => {
  return <Message level={jpe ? 'INFO' : 'WARNING'}>{jpe ? JPE_AVAILABLE : JPE_UNAVAILABLE}</Message>
}
export default JpeMessage
