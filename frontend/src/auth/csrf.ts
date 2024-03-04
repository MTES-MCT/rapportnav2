export const csrfToken = (): string | null => {
  const match = /XSRF-TOKEN=([^;]*)/.exec(document.cookie);
  return match ? match[1] : null;
}
