import type { Metadata } from "next";
import GoogleAnalytics from "./lib/GoogleAnalytics";
// import { Inter } from "next/font/google";
import localFont from "next/font/local";
import "./globals.css";

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

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body className={pretendard.className}>
        {process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS ? (
          <GoogleAnalytics gaId={process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS} />
        ) : null}
        {/* 상태관리 컨텍스트 등은 여기서 감싸주면 됨 */}
        {children}
      </body>
    </html>
  );
}
