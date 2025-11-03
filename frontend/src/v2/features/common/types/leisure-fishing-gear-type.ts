export enum LeisureType {
  TOWED_BUOYS = 'TOWED_BUOYS',
  KITE_SURF = 'KITE_SURF',
  KAYAK = 'KAYAK',
  BOAT_RENTING = 'BOAT_RENTING',
  PARASAILING = 'PARASAILING',
  VNM_HIKES = 'VNM_HIKES',
  NAUTICAL_SKI = 'NAUTICAL_SKI',
  VNM = 'VNM',
  VN_BY_HUMAN_ENERGY = 'VN_BY_HUMAN_ENERGY',
  OTHERS = 'OTHERS'
}

export enum FishingGearType {
  CASHIER = 'CASHIER',
  NET = 'NET',
  LONGLINE = 'LONGLINE',
  OTHERS = 'OTHERS'
}

export const LEISURE_TYPES = {
  [LeisureType.TOWED_BUOYS]: 'Bouées tractées',
  [LeisureType.KITE_SURF]: 'Kite surf',
  [LeisureType.KAYAK]: 'Kayak',
  [LeisureType.BOAT_RENTING]: 'Location bateaux',
  [LeisureType.PARASAILING]: 'Parachute ascensionnel',
  [LeisureType.VNM_HIKES]: 'Randonnées VNM',
  [LeisureType.NAUTICAL_SKI]: 'Ski nautique',
  [LeisureType.VNM]: 'VNM',
  [LeisureType.VN_BY_HUMAN_ENERGY]: 'VN tractée par energie humaine',
  [LeisureType.OTHERS]: 'Autre'
}

export const FISHING_GEAR_TYPES = {
  [FishingGearType.CASHIER]: 'Casier',
  [FishingGearType.NET]: 'Filet',
  [FishingGearType.LONGLINE]: 'Palangre',
  [FishingGearType.OTHERS]: 'Autre'
}
