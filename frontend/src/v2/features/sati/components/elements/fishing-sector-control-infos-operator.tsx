import { FC } from 'react'
import { Contact, SatiParty, SatiPartyType } from '../../../common/types/sati.ts'
import AddContactForm from '../ui/add-contact-form.tsx'

interface FishingSectorControlInfosOperatorProps {
  operator?: SatiParty
  onDelete: () => void
  onChange: (value: SatiParty) => void
}

const FishingSectorControlInfosOperator: FC<FishingSectorControlInfosOperatorProps> = ({
  operator,
  onChange,
  onDelete
}) => {
  const title = `Informations sur représentant du propriétaire`
  const buttonLabel = `Ajouter un représentant du propriétaire`

  const handleChange = (contact: Contact) => {
    onChange({ ...(operator ?? {}), partyType: SatiPartyType.ESTABLISHMENT_OPERATOR, contact })
  }

  return (
    <AddContactForm
      title={title}
      onDelete={onDelete}
      onChange={handleChange}
      buttonLabel={buttonLabel}
      contact={operator?.contact}
    />
  )
}

export default FishingSectorControlInfosOperator
