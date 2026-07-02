import { FC } from 'react'
import { SatiParty } from 'src/v2/features/common/types/sati'
import PartyForm from '../ui/party-form'

interface FishControlInfosOwnerProps {
  name: string
  owner?: SatiParty
}

const FishControlInfosOwner: FC<FishControlInfosOwnerProps> = ({ owner }) => {
  const title = 'Informations sur le propriétaire du navire contrôlé'

  return <PartyForm title={title} party={owner} />
}

export default FishControlInfosOwner
