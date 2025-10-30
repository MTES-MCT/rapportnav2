export type MissionExport = {
  fileName: string
  fileContent: string
}

// if you update this enum, update it too in the core backend and API
export enum ExportMode {
  INDIVIDUAL_MISSION = 'INDIVIDUAL_MISSION', // Export one single mission into one document
  COMBINED_MISSIONS_IN_ONE = 'COMBINED_MISSIONS_IN_ONE', // Export several missions combined into one mission into one document
  MULTIPLE_MISSIONS_ZIPPED = 'MULTIPLE_MISSIONS_ZIPPED' // Export several missions into several documents into a zip
}

// if you update this enum, update it too in the core backend and API
export enum ExportReportType {
  ALL = 'ALL', // all at once
  AEM = 'AEM', // tableaux AEM only
  PATROL = 'PATROL' // rapport de patrouille only
}
