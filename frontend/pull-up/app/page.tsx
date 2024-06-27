import KakaoLogin from "./component/auth/kakaoLogin";

export default function Home() {
  return (
    <main className="bg-blue01 flex h-full w-full flex-col items-center justify-around">
      <p className="h-[200px] w-[200px] border border-solid border-white text-center text-white">
        로고
      </p>
      <KakaoLogin />
    </main>
  );
}
