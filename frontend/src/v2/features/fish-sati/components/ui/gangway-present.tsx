import { Message } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

const GANGWAY_AVAILABLE = `L'unité est montée à bord du navire contrôlé `
const GANGWAY_UNAVAILABLE = `L'unité n'a pas pu monter à bord du navire contrôlé `

interface GangwayPresentProps {
  isUnitBoarded?: boolean
}

const GangwayPresent: FC<GangwayPresentProps> = ({ isUnitBoarded }) => {
  return (
    <Message level={isUnitBoarded ? 'INFO' : 'WARNING'}>
      {isUnitBoarded ? GANGWAY_AVAILABLE : GANGWAY_UNAVAILABLE}
    </Message>
  )
}
export default GangwayPresent
