import { FC } from 'react'
import { Contact, SatiParty } from 'src/v2/features/common/types/sati.ts'
import AddContactForm from '../ui/add-contact-form.tsx'

interface FishControlInfosAgentProps {
  agent?: SatiParty
  onDelete: () => void
  onChange: (value: Contact) => void
}

const FishControlInfosAgent: FC<FishControlInfosAgentProps> = ({ agent, onDelete, onChange }) => {
  const title = `Informations sur l'agent du navire contrôlé`
  const buttonLabel = `Ajouter les informations sur l'agent du navire contrôlé`

  return (
    <AddContactForm
      title={title}
      onDelete={onDelete}
      onChange={onChange}
      buttonLabel={buttonLabel}
      contact={agent?.contact}
    />
  )
}

export default FishControlInfosAgent
