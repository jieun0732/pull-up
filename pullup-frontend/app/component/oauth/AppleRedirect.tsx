"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import useUserStore from "@/stores/useUserStore";

function AppleRedirect() {
  const router = useRouter();
  const { setUser } = useUserStore();

  useEffect(() => {
    const appleLogin = async () => {
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
    appleLogin();
  }, []);

  return null;
}

export default AppleRedirect;
