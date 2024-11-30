"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import LocalStorage from "@/utils/LocalStorage";
import { API } from "@/lib/API";
import useUserStore from "@/stores/useUserStore";
function KakaoRedirect() {
  const router = useRouter();
  const { setUser } = useUserStore();
  useEffect(() => {
    // https://pull-up-snowy.vercel.app/oauth2/kakao?login=false&memberId=15&name=%EA%B9%80%EC%8A%B9%ED%9D%AC&email=senghee9801%40naver.com&provider=kakao
    const kakaoLogin = async () => {
      const queryParams = new URLSearchParams(window.location.search);

      // console.log(window.location.href);
      const firstLogin = queryParams.get("firstLogin");
      const memberId = queryParams.get("memberId");
      const email = queryParams.get("email");
      const name = queryParams.get("name");
      const provider = queryParams.get("provider");

      setUser({
        memberId: Number(memberId),
        email: email || "",
        name: name || "",
        snsProvider: provider || "",
      });

      if (firstLogin === "false") {
        // const response = await fetch(
        //   `${API}/memberAnswers/problems/problem-answers?memberId=${memberId}`,
        //   {
        //     method: "POST",
        //     credentials: "include",
        //     headers: {
        //       "Content-Type": "application/json;charset=utf-8",
        //     },
        //   },
        // );
        // console.log(response);
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
