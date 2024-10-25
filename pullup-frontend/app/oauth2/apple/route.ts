import { NextRequest, NextResponse } from "next/server";
import { parse } from "querystring";

export async function POST(request: NextRequest): Promise<NextResponse> {
  try {
    const body = await request.text();
    const formData = parse(body);

    const code = formData["code"];
    const idToken = formData["id_token"];
    const userData = formData["user"];

    if (!code || !idToken) {
      return NextResponse.json(
        { error: "code 또는 id_token이 누락되었습니다." },
        { status: 400 },
      );
    }

    const newcomment = {
      id: idToken,
      code: code,
    };

    return NextResponse.json(JSON.stringify(newcomment), {
      headers: {
        "Content-Type": "application/json",
      },
      status: 201,
    });

    // let firstName = "";
    // let lastName = "";

    // if (typeof userData === "string") {
    //   try {
    //     const parsedUserData = JSON.parse(userData);
    //     firstName = parsedUserData.firstName;
    //     lastName = parsedUserData.lastName;
    //   } catch (error) {
    //     console.error("userData JSON 파싱 오류:", error);
    //   }
    // } else if (Array.isArray(userData)) {
    //   console.error("userData는 배열입니다:", userData);
    //   // 필요한 경우 배열 처리 로직을 추가할 수 있습니다.
    // }

    // const bodyData = {
    //   code: code.toString(),
    //   idToken: idToken.toString(),
    //   user: userData ? JSON.parse(userData.toString()) : undefined,
    // };

    // const res = await fetch(
    //   `${process.env.NEXT_PUBLIC_SERVER_URL}/login/apple`,
    //   {
    //     method: "POST",
    //     headers: {
    //       "Content-Type": "application/json",
    //     },
    //     body: JSON.stringify(bodyData),
    //   },
    // );

    // if (!res.ok) {
    //   return NextResponse.json(
    //     { error: "서버 요청 실패" },
    //     { status: res.status },
    //   );
    // }

    // const data = (await res.json()) as LoginResponse;
    // const { userId, nickname, profileImageUrl, isSignUpComplete } = data.data;

    // setAuthCookies(data.data);

    // const loginUrl = new URL("/apple/test", request.url);

    // loginUrl.searchParams.set("userId", userId.toString());
    // loginUrl.searchParams.set("nickname", encodeURIComponent(nickname));
    // loginUrl.searchParams.set(
    //   "profileImageUrl",
    //   encodeURIComponent(profileImageUrl),
    // );
    // loginUrl.searchParams.set("isSignUpComplete", isSignUpComplete.toString());

    //   const res = await fetch(
    //     `https://pullup-api.shop/api/oauth2/callback/apple?code=${code}&state=${idToken}&firstName=${firstName}&lastName=${lastName}`,
    //   );

    //   return NextResponse.redirect("/main/sectional", 302);
  } catch (error) {
    console.error("Error handling POST request:", error);
    return NextResponse.json({ error: "Invalid request" }, { status: 400 });
  }
}
