interface PasswordHook {
  generatePassword: (length: number) => string
}

export function usePassword(): PasswordHook {
  const generatePassword = (length: number) => {
    const numbers = '0123456789'
    const symbols = '!@#$%^&*()-_=+[]{}|;:,.<>?'
    const lowercases = 'abcdefghijklmnopqrstuvwxyz'
    const uppercases = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'

    const debut =
      numbers.charAt(Math.floor(Math.random() * numbers.length)) +
      symbols.charAt(Math.floor(Math.random() * symbols.length)) +
      lowercases.charAt(Math.floor(Math.random() * lowercases.length)) +
      uppercases.charAt(Math.floor(Math.random() * uppercases.length))

    const chars = numbers + symbols + lowercases + uppercases
    let end = ''
    for (let i = debut.length; i < length; i++) {
      end += chars.charAt(Math.floor(Math.random() * chars.length))
    }

    return (debut + end)
      .split('')
      .sort(() => 0.5 - Math.random())
      .join('')
  }

  return { generatePassword }
}
