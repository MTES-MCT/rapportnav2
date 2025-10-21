import { Message } from '@mtes-mct/monitor-ui'

const MissionActionNaControlWarning: React.FC = () => {
  return (
    <Message level="INFO">
      Pour la saisie des contrôles de la pêche et de l’environnement marin,
      <br />
      veuillez appeler les centres concernés.
      <br />
      Pêche : CNSP / Environnement Marin : CACEM
    </Message>
  )
}

export default MissionActionNaControlWarning
