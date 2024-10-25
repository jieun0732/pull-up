"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function AppleRedirect() {
  const router = useRouter();
  const [code, setCode] = useState<string | null>(null);
  const [state, setState] = useState<string | null>(null);
  const [firstName, setFirstName] = useState<string | null>(null);
  const [lastName, setLastName] = useState<string | null>(null);

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const codeFromUrl = urlParams.get("code");
    const stateFromUrl = urlParams.get("state");
    const user = urlParams.get("user") || "";

    if (codeFromUrl) {
      setCode(codeFromUrl);
    }
    if (stateFromUrl) {
      setState(stateFromUrl);
    }

    if (user) {
      try {
        const userObject = JSON.parse(user);
        setFirstName(userObject.name.firstName);
        setLastName(userObject.name.lastName);
      } catch (error) {
        console.error("JSON 파싱 오류:", error);
      }
    }

    if (codeFromUrl && state) {
      const callbackUrl = `https://pullup-api.shop/api/oauth2/callback/apple?code=${code}&state=${state}&firstName=${firstName}&lastName=${lastName}`;
      appleLogin(callbackUrl);
    }
  }, []);

  const appleLogin = async (callbackUrl: string) => {
    let isSuccessed = false; // 로컬 변수를 함수 내부로 이동

    try {
      const res = await fetch(callbackUrl);

      if (res.ok) {
        isSuccessed = true;
      }
    } catch (error) {
      console.error("로그인 요청 중 오류 발생:", error);
    }

    if (isSuccessed) {
      router.replace("/main/sectional");
    }
  };

  return null;
}
