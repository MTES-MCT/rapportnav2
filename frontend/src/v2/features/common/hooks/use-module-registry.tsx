import { ActionTypeEnum } from '@common/types/env-mission-types'
import { ModuleType } from '../types/module-type'

export type DropdownSubItem = { type: ActionTypeEnum }
export type DropdownItem = DropdownSubItem & { subItems?: DropdownSubItem[] }

const TIME_LINE_DROPDOWN_PAM_ITEMS: DropdownItem[] = [
  { type: ActionTypeEnum.CONTROL },
  { type: ActionTypeEnum.NOTE },
  { type: ActionTypeEnum.RESCUE },
  {
    type: ActionTypeEnum.OTHER,
    subItems: [
      { type: ActionTypeEnum.NAUTICAL_EVENT },
      { type: ActionTypeEnum.BAAEM_PERMANENCE },
      { type: ActionTypeEnum.VIGIMER },
      { type: ActionTypeEnum.ANTI_POLLUTION },
      { type: ActionTypeEnum.ILLEGAL_IMMIGRATION },
      { type: ActionTypeEnum.PUBLIC_ORDER },
      { type: ActionTypeEnum.REPRESENTATION }
    ]
  }
]

const TIME_LINE_DROPDOWN_ULAM_ITEMS: DropdownItem[] = [
  {
    type: ActionTypeEnum.CONTROL,
    subItems: [{ type: ActionTypeEnum.CONTACT }, { type: ActionTypeEnum.SURVEILLANCE }]
  },
  {
    type: ActionTypeEnum.OTHER,
    subItems: [
      { type: ActionTypeEnum.NAUTICAL_EVENT },
      { type: ActionTypeEnum.BAAEM_PERMANENCE },
      { type: ActionTypeEnum.VIGIMER }
    ]
  }
]

const MODULE_REGISTRY = {
  [ModuleType.PAM]: {
    timelineDropdownItems: TIME_LINE_DROPDOWN_PAM_ITEMS
  },
  [ModuleType.ULAM]: {
    timelineDropdownItems: TIME_LINE_DROPDOWN_ULAM_ITEMS
  }
}

interface ModuleRegistryHook {
  timelineDropdownItems: DropdownItem[]
}

export function useModuleRegistry(moduleType: ModuleType): ModuleRegistryHook {
  return { ...MODULE_REGISTRY[moduleType] }
}
