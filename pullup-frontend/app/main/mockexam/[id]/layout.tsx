"use client";
import { SWRConfig } from "swr";

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <>
      <SWRConfig
        value={{
          fetcher: (...args: [string]) =>
            fetch(...args).then((res) => res.json()),
        }}
      >
        {children}
      </SWRConfig>
    </>
  );
}
