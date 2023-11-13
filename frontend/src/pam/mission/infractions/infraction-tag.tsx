import React from 'react'
import { Accent, Tag } from '@mtes-mct/monitor-ui'

interface InfractionTagProps {
  text: string
}

const InfractionTag: React.FC<InfractionTagProps> = ({ text }) => {
  return <Tag accent={Accent.PRIMARY}>{text}</Tag>
}

export default InfractionTag
