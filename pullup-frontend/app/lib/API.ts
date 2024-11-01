export const API = process.env.NEXT_PUBLIC_API;

export const APPLE_API = process.env.NEXT_APPLE_API;

export const KAKAO_API = process.env.NEXT_KAKAO_API;

export const KAKAO_KEY = process.env.NEXT_KAKAO_KEY;

export const KAKAO_URL = process.env.NEXT_KAKAO_URL;

export const KAKAO_REDIRECT_URI = process.env.NEXT_KAKAO_REDIRECT_URI;

export const APPLE_REDIRECT_URI = process.env.NEXT_APPLE_REDIRECT_URI;

export const fetcher = (...args: [string]) =>
  fetch(...args).then((res) => res.json());
