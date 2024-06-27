"use client";

import Image from "next/image";
import kakaoLogin from "@/assets/auth/kakao_login_medium_wide.png";
import { useRouter } from "next/navigation";

export default function KakaoLogin() {
  const router = useRouter();

  const handleLogin = () => {
    router.push("main/sectional");
  };
  return (
    <Image
      src={kakaoLogin}
      alt="kakaoLogin"
      placeholder="blur" // Optional blur-up while loading
      onClick={handleLogin}
    />
  );
}
