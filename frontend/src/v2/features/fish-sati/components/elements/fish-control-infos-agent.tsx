import { FC } from 'react'
import { Contact, SatiParty, SatiPartyType } from '../../../common/types/sati.ts'
import AddContactForm from '../ui/add-contact-form.tsx'

interface FishControlInfosAgentProps {
  agent?: SatiParty
  onDelete: () => void
  onChange: (value: SatiParty) => void
}

const FishControlInfosAgent: FC<FishControlInfosAgentProps> = ({ agent, onDelete, onChange }) => {
  const title = `Informations sur l'agent du navire contrôlé`
  const buttonLabel = `Ajouter les informations sur l'agent du navire contrôlé`

  const handleChange = (contact: Contact) => {
    onChange({ ...agent, partyType: SatiPartyType.VESSEL_AGENT, contact })
  }

  return (
    <AddContactForm
      title={title}
      onDelete={onDelete}
      onChange={handleChange}
      buttonLabel={buttonLabel}
      contact={agent?.contact}
    />
  )
}

export default FishControlInfosAgent
