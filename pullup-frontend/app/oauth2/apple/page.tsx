"use client";

import { useSearchParams } from "next/navigation";
import { useRouter } from "next/router";

export default function Page() {
  const searchParams = useSearchParams();
  const code = searchParams.get("code");
  const state = searchParams.get("state");
  const user = searchParams.get("user") || "";
  const userObject = JSON.parse(user);
  const firstName = userObject.name.firstName;
  const lastName = userObject.name.lastName;
  const router = useRouter();

  router.push(
    `https://pullup-api.shop/api/oauth2/callback/apple?code=${code}&state=${state}&firstName=${firstName}&lastName=${lastName}`,
  );

  return <div>애플 로그인 중임</div>;
}
