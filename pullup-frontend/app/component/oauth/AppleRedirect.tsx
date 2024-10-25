"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function Page() {
  const router = useRouter();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");
    const state = urlParams.get("state");
    const user = urlParams.get("user") || "";

    let firstName = "";
    let lastName = "";

    if (user) {
      try {
        const userObject = JSON.parse(user);
        firstName = userObject.name.firstName;
        lastName = userObject.name.lastName;
      } catch (error) {
        console.error("JSON 파싱 오류:", error);
      }
    }

    if (code && state) {
      // URL로 리다이렉트
      const callbackUrl = `https://pullup-api.shop/api/oauth2/callback/apple?code=${code}&state=${state}&firstName=${firstName}&lastName=${lastName}`;
      router.push(callbackUrl);
    }
  }, [router]);

  return null;
}
