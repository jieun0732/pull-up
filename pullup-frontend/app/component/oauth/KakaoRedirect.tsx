"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/router";
import { UserLoginStatus } from "@/types/userType";
import LocalStorage from "@/utils/LocalStorage";

const KakaoRedirect = () => {
  const [code, setCode] = useState<string | null>(null);
  const router = useRouter();
  let isSuccessed = false;
  useEffect(() => {
    if (typeof window !== "undefined") {
      new URL(window.location.href).searchParams.get("code") &&
        setCode(new URL(window.location.href).searchParams.get("code"));
      // 1. 인가코드 추출
    }
  }, []);

  const kakaoLogin = async () => {
    const response = await fetch(
      "https://pullup-api.shop/api/oauth2/login/apple",
      {
        method: "POST",
        body: JSON.stringify({
          code: code,
        }),
        headers: {
          "Content-Type": "application/json;charset=utf-8",
        },
      },
    );
    const data: UserLoginStatus = await response.json();
    console.log("UserLoginStatus");
    console.log(data);
    LocalStorage.setItem("memberId", String(data.memberId));
    router.push("/main/sectional");
  };

  useEffect(() => {
    code !== null && kakaoLogin();
    // 2. 인가코드 추출되면 kakaoLogin 로직 실행
  }, [code]);

  return null;
};

export default KakaoRedirect;
