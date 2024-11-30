import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
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
    const createdTime = new Date(createdDate).getTime();
    const limitTime = durationInMinutes * 60 * 1000;

    if (!createdDate) {
      router.push("/main/mockexam"); // 기본 페이지로 이동
      return;
    }

    const handleExamResult = async () => {
      try {
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
        console.log({
          examId: examId,
          answerSheets: selectedAnswers,
        });
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        const result = await response.json();
        router.push("/main/mockexam/report");
      } catch (error) {
        console.error(error);
      } finally {
        // router.push("/main/mockexam/report");
      }
    };

    // 경과 시간 계산
    const elapsed = currentTime - createdTime; // 경과 시간(밀리초)
    const remainingTime = durationInSeconds - Math.floor(elapsed / 1000);

    // durationInMinutes가 지나면 페이지 이동
    if (elapsed >= limitTime) {
      handleExamResult();
      return;
    }

    // 타이머가 남은 시간이 없으면 이동
    if (remainingTime <= 0) {
      handleExamResult();
      return;
    }

    setTimeLeft(remainingTime); // 남은 시간 설정

    // 타이머 설정
    const interval = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1 && step >= 2) {
          clearInterval(interval);
          handleExamResult();
          return 0; // 타이머가 끝났음을 표시
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(interval); // 컴포넌트 언마운트 시 정리
  }, [router, durationInSeconds, step, createdDate]); // createdDate를 의존성 배열에 추가

  // 시간 포맷팅 함수
  const formatTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${String(minutes).padStart(2, "0")}:${String(secs).padStart(2, "0")}`;
  };

  return formatTime(timeLeft);
};

export default useTimer;
