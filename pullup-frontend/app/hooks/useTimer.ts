import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import LocalStorage from "@/utils/LocalStorage";
import { API } from "@/lib/API";
import useExamStore from "@/stores/useExamStore";
import { MockExamReportType } from "@/types/mockexam/mockexamReport";

const useTimer = (dep: string, durationInMinutes: number, step: number) => {
  const router = useRouter();
  const durationInSeconds = durationInMinutes * 60; // 분을 초로 변환
  const [timeLeft, setTimeLeft] = useState(durationInSeconds);
  const { selectedAnswers, examId, time: createdDate } = useExamStore();

  useEffect(() => {
    if (step < 2) {
      setTimeLeft(durationInSeconds); // step이 2 미만일 때는 초기 시간 설정
      return;
    }

    const currentTime = Date.now();
    if (!createdDate) {
      const currentTimeISO = new Date().toISOString(); // 현재 시간을 ISO 형식으로 저장
      localStorage.setItem("time", currentTimeISO); // localStorage에 현재 시간 저장
      router.push("/main/mockexam"); // 기본 페이지로 이동
      return;
    }

    const createdTime = new Date(createdDate).getTime();
    const limitTime = durationInMinutes * 60 * 1000;
    // 30분이 지나면 이동
    if (currentTime - createdTime > limitTime) {
      console.log("Current Time:", new Date(currentTime).toLocaleString());
      console.log("Created Time:", new Date(createdTime).toLocaleString());
      console.log(
        currentTime - createdTime,
        "--------------------------------",
      );

      const handleExamResult = async () => {
        console.log(
          currentTime,
          createdTime,
          limitTime,
          "--------------------------------",
        );
        try {
          console.log("Grade-------------------------");
          const response = await fetch(`${API}/exams/mock-exam/grade`, {
            method: "POST",
            // credentials: "include",
            headers: {
              Accept: "*/*",
              "Content-Type": "application/json",
            },
            body: JSON.stringify({
              examId: examId,
              answerSheets: selectedAnswers,
            }),
          });

          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          const result: MockExamReportType = await response.json();
        } catch (error) {
          console.error(error);
        } finally {
          router.push("/main/mockexam/report");
        }
      };

      handleExamResult();
      return;
    }

    // 남은 시간 계산
    const elapsed = Math.floor((currentTime - createdTime) / 1000); // 경과 시간(초)
    const remainingTime = durationInSeconds - elapsed;

    // 타이머가 남은 시간이 없으면 이동
    if (remainingTime <= 0) {
      router.push("/main/mockexam/report"); // 타이머가 끝났음을 표시
      return;
    }

    setTimeLeft(remainingTime); // 남은 시간 설정

    // 타이머 설정
    const interval = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1 && step >= 2) {
          clearInterval(interval);
          router.push("/main/mockexam/report"); // 타이머가 끝나면 이동
          return 0; // 타이머가 끝났음을 표시
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(interval); // 컴포넌트 언마운트 시 정리
  }, [router, durationInSeconds, step]);

  // 시간 포맷팅 함수
  const formatTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${String(minutes).padStart(2, "0")}:${String(secs).padStart(2, "0")}`;
  };

  return formatTime(timeLeft);
};

export default useTimer;
