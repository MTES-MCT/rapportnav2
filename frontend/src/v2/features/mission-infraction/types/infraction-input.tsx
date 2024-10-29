import { GearInfraction, LogbookInfraction, OtherInfraction, SpeciesInfraction } from '@common/types/fish-mission-types'
import { Infraction } from '@common/types/infraction-types'

export type FishNavInfraction = LogbookInfraction | GearInfraction | SpeciesInfraction | OtherInfraction | Infraction
//TODO: update Fish infraction to add id, natinf to natinfs plural in array, transfert comment into observations
