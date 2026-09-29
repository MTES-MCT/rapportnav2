import { object, ObjectShape, string } from 'yup'
import { SectorFishingType } from '../../common/types/sector-types'

export const isLandingSite = (sectorEstablishmentType?: string) =>
  sectorEstablishmentType === SectorFishingType.LANDING_SITE

export const isFishAuction = (sectorEstablishmentType?: string) =>
  sectorEstablishmentType === SectorFishingType.FISH_AUCTION

export const isControlLocation = (sectorEstablishmentType?: string) =>
  isLandingSite(sectorEstablishmentType) || isFishAuction(sectorEstablishmentType)

export const getControlSectorFishingSchema = (isMissionFinished: boolean): ObjectShape => ({
  sectorEstablishmentType: isMissionFinished ? string().required() : string().nullable(),
  establishment: isMissionFinished
    ? object().when('sectorEstablishmentType', {
        is: (sectorEstablishmentType?: string) => !isControlLocation(sectorEstablishmentType),
        then: schema => schema.shape({ name: string().required() }).required(),
        otherwise: schema => schema.nullable()
      })
    : object().nullable(),
  portLocode: isMissionFinished
    ? string().when('sectorEstablishmentType', {
        is: isLandingSite,
        then: schema => schema.required(),
        otherwise: schema => schema.nullable()
      })
    : string().nullable(),
  fishAuction: isMissionFinished
    ? object().when('sectorEstablishmentType', {
        is: isFishAuction,
        then: schema => schema.required(),
        otherwise: schema => schema.nullable()
      })
    : object().nullable()
})
