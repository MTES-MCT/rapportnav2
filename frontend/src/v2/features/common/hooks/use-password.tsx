interface PasswordHook {
  generatePassword: (length: number) => string
}

export function usePassword(): PasswordHook {
  const secureRandom = (max: number): number => {
    const array = new Uint32Array(1)
    crypto.getRandomValues(array)
    return array[0] % max
  }

  const generatePassword = (length: number) => {
    const numbers = '0123456789'
    const symbols = '!@#$%^&*()-_=+[]{}|;:,.<>?'
    const lowercases = 'abcdefghijklmnopqrstuvwxyz'
    const uppercases = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'

    const debut =
      numbers.charAt(secureRandom(numbers.length)) +
      symbols.charAt(secureRandom(symbols.length)) +
      lowercases.charAt(secureRandom(lowercases.length)) +
      uppercases.charAt(secureRandom(uppercases.length))

    const chars = numbers + symbols + lowercases + uppercases
    let end = ''
    for (let i = debut.length; i < length; i++) {
      end += chars.charAt(secureRandom(chars.length))
    }

    const result = (debut + end).split('')
    for (let i = result.length - 1; i > 0; i--) {
      const j = secureRandom(i + 1)
      ;[result[i], result[j]] = [result[j], result[i]]
    }
    return result.join('')
  }

  return { generatePassword }
}
