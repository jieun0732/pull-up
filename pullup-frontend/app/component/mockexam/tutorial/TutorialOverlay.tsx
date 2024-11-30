"use client";

import { Dispatch, SetStateAction } from "react";
import { API } from "@/lib/API";
import useUserStore from "@/stores/useUserStore";

interface TutorialOverlayProps {
  problemId: string;
  step: number;
  setStep: Dispatch<SetStateAction<number>>;
  setTutorialFinished: (prevState: boolean) => void;
}

const TutorialOverlay = ({
  problemId,
  step,
  setStep,
  setTutorialFinished,
}: TutorialOverlayProps) => {
  const { user } = useUserStore();
  if (problemId !== "1") return;
  return (
    step < 2 && (
      <div
        onClick={() => {
          const currentTimeISO = new Date().toISOString(); // 현재 시간을 ISO 형식으로 저장
          localStorage.setItem("time", currentTimeISO); // localStorage에 현재 시간 저장
          setStep((prevStep) => prevStep + 1);
          if (step == 1) {
            setTutorialFinished(true);
            fetch(`${API}/members/tutorial/${user.memberId}`, {
              method: "PATCH",
              // credentials: "include",
            });
          }
        }}
        className="absolute left-0 top-0 z-20 h-screen w-full bg-black pb-7 pt-20 opacity-80"
      />
    )
  );
};

export default TutorialOverlay;
