"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import useUserStore from "@/stores/useUserStore";
function KakaoRedirect() {
  const router = useRouter();
  const { setUser } = useUserStore();
  useEffect(() => {
    const kakaoLogin = async () => {
      const queryParams = new URLSearchParams(window.location.search);

      console.log(window.location.href);
      const firstLogin = queryParams.get("firstLogin");
      const memberId = queryParams.get("memberId");
      const email = queryParams.get("email");
      const name = queryParams.get("name");
      const provider = queryParams.get("provider");
      console.log(firstLogin, memberId, email, name, provider);

      setUser({
        memberId: Number(memberId),
        email: email || "",
        name: name || "",
        snsProvider: provider || "",
      });

      if (firstLogin === "false") {
        router.push("/main/sectional");
      } else {
        router.push("/");
      }
    };

    kakaoLogin();
  }, []);

  return null;
}

export default KakaoRedirect;
