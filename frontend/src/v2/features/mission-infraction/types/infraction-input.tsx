import { GearInfraction, LogbookInfraction, OtherInfraction, SpeciesInfraction } from '@common/types/fish-mission-types'

export type FishInfraction = LogbookInfraction | GearInfraction | SpeciesInfraction | OtherInfraction
//TODO: update Fish infraction to add id, natinf to natinfs plural in array, transfert comment into observations
