import { Accent, Tag } from '@mtes-mct/monitor-ui'
import { MissionSourceEnum } from '../env-mission-types'

interface MissionOpenByTagProps {
  missionSource?: MissionSourceEnum
}

const MissionOpenByTag: React.FC<MissionOpenByTagProps> = ({ missionSource }) => {
  return (
    <Tag accent={missionSource === MissionSourceEnum.RAPPORTNAV ? Accent.PRIMARY : Accent.SECONDARY}>
      {missionSource === MissionSourceEnum.RAPPORTNAV
        ? "Ouverte par l'unité"
        : missionSource === MissionSourceEnum.MONITORENV || missionSource === MissionSourceEnum.POSEIDON_CACEM
        ? 'Ouverte par le CACEM'
        : missionSource === MissionSourceEnum.MONITORFISH || missionSource === MissionSourceEnum.POSEIDON_CNSP
        ? 'Ouverte par le CNSP'
        : 'Ouverte par N/A'}
    </Tag>
  )
}

export default MissionOpenByTag
