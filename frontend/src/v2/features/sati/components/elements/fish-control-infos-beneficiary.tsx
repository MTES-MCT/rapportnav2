import { FC } from 'react'
import { Contact, SatiParty, SatiPartyType } from '../../../common/types/sati.ts'
import AddContactForm from '../ui/add-contact-form.tsx'

interface FishControlInfosBeneficiaryProps {
  beneficiary?: SatiParty
  onDelete: () => void
  onChange: (value: SatiParty) => void
}

const FishControlInfosBeneficiary: FC<FishControlInfosBeneficiaryProps> = ({ onDelete, onChange, beneficiary }) => {
  const title = `Informations sur le bénéficiaire effectif`
  const buttonLabel = `Ajouter les informations sur le bénéficiaire effectif`

  const handleChange = (contact: Contact) => {
    onChange({ ...beneficiary, partyType: SatiPartyType.VESSEL_BENEFICIARY, contact })
  }

  return (
    <AddContactForm
      title={title}
      onDelete={onDelete}
      onChange={handleChange}
      buttonLabel={buttonLabel}
      contact={beneficiary?.contact}
    />
  )
}

export default FishControlInfosBeneficiary
