import type { Metadata } from "next";
import GoogleAnalytics from "./lib/GoogleAnalytics";
// import { Inter } from "next/font/google";
import localFont from "next/font/local";
import "./globals.css";
import Script from "next/script";
import Head from "next/head";
// const inter = Inter({ subsets: ["latin"] });

const pretendard = localFont({
  src: "./assets/font//PretendardVariable.woff2",
  display: "swap",
  weight: "45 920",
  variable: "--font-pretendard",
});

export const metadata: Metadata = {
  title: "풀업",
  description: "풀업 설명",
};

declare global {
  interface Window {
    Kakao: any;
  }
}
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <Head>
        <link
          rel="preload"
          href="https://t1.kakaocdn.net/kakao_js_sdk/2.7.2/kakao.min.js"
          as="script"
        />
      </Head>
      <body className={pretendard.className}>
        {process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS ? (
          <GoogleAnalytics gaId={process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS} />
        ) : null}
        {children}
        {/* <Script
          type="text/javascript"
          src="https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/ko_KR/appleid.auth.js"
        /> */}
        <Script
          type="text/javascript"
          src="https://t1.kakaocdn.net/kakao_js_sdk/2.7.2/kakao.min.js"
          integrity="sha384-TiCUE00h649CAMonG018J2ujOgDKW/kVWlChEuu4jK2vxfAAD0eZxzCKakxg55G4"
          crossOrigin="anonymous"
        />
      </body>
    </html>
  );
}
