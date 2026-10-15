import { FC } from 'react'
import { SatiParty } from '../../../common/types/sati.ts'
import PartyForm from '../ui/party-form.tsx'

interface FishControlInfosOperatorProps {
  operator?: SatiParty
}

const FishControlInfosOperator: FC<FishControlInfosOperatorProps> = ({ operator }) => {
  const title = `Informations sur l'affréteur du navire contrôlé`
  return <PartyForm title={title} party={operator} />
}

export default FishControlInfosOperator
