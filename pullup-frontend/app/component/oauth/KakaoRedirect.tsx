"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import LocalStorage from "@/utils/LocalStorage";
import { APPLE_API, API } from "@/lib/API";
import { UserLoginStatus } from "@/types/userType";

function KakaoRedirect() {
  const router = useRouter();

  useEffect(() => {
    // https://pull-up-snowy.vercel.app/oauth2/kakao?login=false&memberId=15&name=%EA%B9%80%EC%8A%B9%ED%9D%AC&email=senghee9801%40naver.com&provider=kakao
    const kakaoLogin = async () => {
      const queryParams = new URLSearchParams(window.location.search);
      console.log(
        "kakao login href=================================================",
      );
      console.log(window.location.href);
      const firstLogin = queryParams.get("firstLogin");
      const memberId = queryParams.get("memberId");
      const email = queryParams.get("email");
      const name = queryParams.get("name");
      const provider = queryParams.get("provider");

      console.log(memberId); // 15
      console.log(firstLogin); // false
      console.log(email); // senghee9801@naver.com
      console.log(name); // 김승희
      console.log(provider); // kakao

      if (memberId) {
        LocalStorage.setItem("memberId", memberId);
        if (email) LocalStorage.setItem("email", email);
        if (name) LocalStorage.setItem("name", name);
        if (provider) LocalStorage.setItem("provider", provider);
        console.log("kakao redirect");
        console.log(memberId, email, name, provider);
        if (firstLogin === "false") {
          const response = await fetch(
            `${API}/memberAnswers/problems/problem-answers?memberId=${memberId}`,
            {
              method: "POST",
              credentials: "include",
              headers: {
                "Content-Type": "application/json;charset=utf-8",
              },
            },
          );
          console.log(response);
        }
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
