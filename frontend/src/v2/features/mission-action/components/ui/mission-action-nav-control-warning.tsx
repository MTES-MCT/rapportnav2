import { FC } from 'react'
import { Message } from '@mtes-mct/monitor-ui'

const MissionActionNavControlWarning: FC = () => {
  return (
    <Message level="WARNING">
      Veuillez contacter respectivement le CNSP et le CACEM pour la saisie des contrôles de pêche pro embarquée
      (contrôle mer, débarquement et aériens) et de l'environnement marin.
      <br />
      Les centres ouvriront le contrôle.
    </Message>
  )
}

export default MissionActionNavControlWarning
