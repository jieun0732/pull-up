"use client";

import { Dispatch, SetStateAction } from "react";

interface TutorialOverlayProps {
  problemId: string;
  step: number;
  setStep: Dispatch<SetStateAction<number>>;
}

const TutorialOverlay = ({
  problemId,
  step,
  setStep,
}: TutorialOverlayProps) => {
  if (problemId !== "1") return;
  return (
    step < 2 && (
      <div
        onClick={() => setStep((prevStep) => prevStep + 1)}
        className="absolute left-0 top-0 z-20 h-screen w-full bg-black pb-7 pt-14 opacity-80"
      />
    )
  );
};

export default TutorialOverlay;
