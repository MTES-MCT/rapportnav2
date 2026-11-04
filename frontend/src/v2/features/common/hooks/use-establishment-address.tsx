import { Establishment } from '../types/etablishment'
import { Address } from '../types/sati'

interface EstablishmentAddressHook {
  establishmentToAddress: (establishment?: Establishment) => Address | undefined
  addressToEstablishment: (address?: Address, establishment?: Establishment) => Establishment
  resetAddress: (establishment?: Establishment) => Establishment
  resetCompany: (establishment?: Establishment) => Establishment
}

export function useEstablishmentAddress(): EstablishmentAddressHook {
  const establishmentToAddress = (establishment?: Establishment): Address | undefined => {
    if (!establishment) return undefined
    return {
      street: establishment.address,
      town: establishment.city,
      zipcode: establishment.zipcode,
      country: establishment.country
    }
  }

  const addressToEstablishment = (address?: Address, establishment?: Establishment): Establishment => ({
    ...establishment,
    address: address?.street,
    city: address?.town,
    zipcode: address?.zipcode,
    country: address?.country
  })

  const resetAddress = (establishment?: Establishment): Establishment =>
    addressToEstablishment(undefined, establishment)

  const resetCompany = (establishment?: Establishment): Establishment => ({
    ...establishment,
    name: undefined,
    siren: undefined,
    siret: undefined,
    isHeadquarter: undefined
  })

  return { establishmentToAddress, addressToEstablishment, resetAddress, resetCompany }
}
