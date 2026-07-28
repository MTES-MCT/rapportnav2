import { Message } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

const GANGWAY_AVAILABLE = `L’échelle de coupée était mise à disposition et praticable `
const GANGWAY_UNAVAILABLE = `L’échelle de coupée n’a pas été mise à disposition / n’était pas praticable`

interface GangwayPresentProps {
  withGangWay?: boolean
}

const GangwayPresent: FC<GangwayPresentProps> = ({ withGangWay }) => {
  return (
    <Message level={withGangWay ? 'INFO' : 'WARNING'}>{withGangWay ? GANGWAY_AVAILABLE : GANGWAY_UNAVAILABLE}</Message>
  )
}
export default GangwayPresent
