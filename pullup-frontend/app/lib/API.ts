export const API = process.env.NEXT_PUBLIC_API;

export const fetcher = (...args: [string]) =>
  fetch(...args).then((res) => res.json());
