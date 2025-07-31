import Text from '@common/components/ui/text.tsx'
import { FC, useState } from 'react'
import { Container, Stack } from 'rsuite'
import { Inquiry } from '../../../common/types/inquiry.ts'
import { User } from '../../../common/types/user.ts'
import InquiryListHeader from '../ui/inquiry-list-header.tsx'
import InquiryListItem from '../ui/inquiry-list-item.tsx'

interface InquiryListProps {
  inquiries?: Inquiry[]
  user?: User
}

const InquiryList: FC<InquiryListProps> = ({ inquiries, user }) => {
  const [openIndex, setOpenIndex] = useState<number | null>(null)

  return (
    <Container>
      <Stack direction="column" alignItems="flex-start" spacing="0.2rem" style={{ width: '100%' }}>
        <Stack.Item style={{ width: '100%' }}>
          <InquiryListHeader />
        </Stack.Item>

        <Stack.Item style={{ width: '100%' }}>
          <Stack
            direction={'column'}
            spacing={'0.2rem'}
            style={{ width: '100%', overflowY: 'scroll', height: '50vh', minHeight: '50vh', maxHeight: '50vh' }}
          >
            <Stack.Item style={{ width: '100%' }}>
              {!inquiries?.length ? (
                <Stack.Item alignSelf={'center'} style={{ marginTop: '10rem' }}>
                  <Text as={'h3'}>Aucune mission pour cette période de temps</Text>
                </Stack.Item>
              ) : (
                inquiries?.map((inquiry, index) => (
                  <InquiryListItem
                    user={user}
                    index={index}
                    inquiry={inquiry}
                    openIndex={openIndex}
                    length={inquiries.length}
                    setOpenIndex={setOpenIndex}
                    key={`${inquiry.id}-${index}`}
                    onSelect={id => console.log(id)}
                  />
                ))
              )}
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    </Container>
  )
}

export default InquiryList
