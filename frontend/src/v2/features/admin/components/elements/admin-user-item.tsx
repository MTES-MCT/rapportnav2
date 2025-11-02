import React from 'react'
import { Stack } from 'rsuite'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'name', label: 'Nom', width: 300 },
  { key: 'controlUnits', label: 'Unité de contrôle', width: 200 },
  { key: 'createdAt', label: 'Date de Creation', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 240 },
  { key: 'deletedAt', label: 'Date de Creation', width: 200 }
]

type AdminUserProps = {}

const AdminUserItem: React.FC<AdminUserProps> = () => {
  return (
    <Stack
      direction="column"
      alignItems="flex-start"
      spacing="0.2rem"
      style={{ padding: '0 11.5rem', marginTop: '3rem' }}
    >
      ERZEARZE
    </Stack>
  )
}

export default AdminUserItem
