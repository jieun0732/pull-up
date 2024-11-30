export const API = process.env.NEXT_PUBLIC_API;

export const APPLE_API = process.env.NEXT_PUBLIC_APPLE_API;

export const KAKAO_API = process.env.NEXT_PUBLIC_KAKAO_API;

export const KAKAO_KEY = process.env.NEXT_PUBLIC_KAKAO_KEY;

export const KAKAO_URL = process.env.NEXT_PUBLIC_KAKAO_URL;

export const KAKAO_REDIRECT_URI = process.env.NEXT_PUBLIC_KAKAO_REDIRECT_URI;

export const APPLE_REDIRECT_URI = process.env.NEXT_PUBLIC_APPLE_REDIRECT_URI;

export const fetcher = (...args: [string]) =>
  fetch(...args, {
    // credentials: "include",
  }).then((res) => res.json());

export const patchFetcher = (url: string) =>
  fetch(url, {
    method: "PATCH", // PATCH 요청으로 설정
    headers: {
      "Content-Type": "application/json", // 요청 본문의 타입 설정
    },
    // credentials: "include", // 필요시 주석 해제
  }).then((res) => {
    if (!res.ok) {
      throw new Error("Network response was not ok"); // 에러 처리
    }
    return res.json(); // JSON으로 응답 처리
  });
