"use client";

import { useParams, useRouter } from "next/navigation";
import { sections } from "@/constants/constants";
import useTimer from "@/hooks/useTimer";

export default function Layout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const router = useRouter();
  const params = useParams<{ id: string }>();
  const currentSection = sections[params.id];
  const { timer, isRunning, startTimer, stopTimer } = useTimer({
    redirectPath: "/main", // 타이머가 종료되면 이동할 경로
    initialTimer: 60, // 초기 타이머 시간 (60초)
  });

  return (
    <>
      <div className="flex h-full w-full flex-col items-center bg-green-100 text-lg text-red-500">
        <p>{currentSection}</p>
        <div>
          <h1>Check Page</h1>
          <p>Time left: {timer} seconds</p>
          {isRunning ? (
            <button onClick={stopTimer}>Stop Timer</button>
          ) : (
            <button onClick={startTimer}>Start Timer</button>
          )}
        </div>

        {children}
      </div>
    </>
  );
}
