// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom'

const globalThisAny: any = globalThis
globalThisAny.fetch = fetch
globalThisAny.Headers = Headers
globalThisAny.Request = Request
globalThisAny.Response = Response
