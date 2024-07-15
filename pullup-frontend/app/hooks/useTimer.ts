import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useTimerStore } from "@/stores/useTimerStore";

interface UseTimerProps {
  redirectPath: string;
  initialTimer?: number; // 초기 타이머 시간 설정
}

const useTimer = ({ redirectPath, initialTimer = 60 }: UseTimerProps) => {
  const { timer, timerStatus, setTimer, setTimerStatus } = useTimerStore();
  const [isRunning, setIsRunning] = useState(timerStatus);
  const router = useRouter();

  useEffect(() => {
    // 새로고침이나 페이지 이동 시에도 타이머 유지
    if (!timerStatus) {
      setTimerStatus(true);
      setTimer(initialTimer);
      setIsRunning(true);
    }
  }, [setTimer, initialTimer]);

  useEffect(() => {
    let interval: ReturnType<typeof setInterval> | null = null;

    if (isRunning) {
      interval = setInterval(() => {
        setTimer(timer - 1);
      }, 1000);
    }

    return () => {
      if (interval) {
        clearInterval(interval);
      }
    };
  }, [isRunning, setTimer, timer]);

  useEffect(() => {
    if (timer === 0 && timerStatus == true) {
      setIsRunning(false);
      setTimerStatus(false);
      router.push(redirectPath);
    }
  }, [timer, setTimerStatus, router, redirectPath]);

  const startTimer = () => {
    setIsRunning(true);
    setTimerStatus(true);
  };

  const stopTimer = () => {
    setIsRunning(false);
    setTimerStatus(false);
  };

  return { timer, isRunning, startTimer, stopTimer };
};

export default useTimer;
