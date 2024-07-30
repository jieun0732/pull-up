"use client";

import { Dispatch, SetStateAction } from "react";

interface TutorialOverlayProps {
  step: number;
  setStep: Dispatch<SetStateAction<number>>;
}

const TutorialOverlay = ({ step, setStep }: TutorialOverlayProps) => {
  return (
    step < 2 && (
      <div
        onClick={() => setStep((prevStep) => prevStep + 1)}
        className="absolute left-0 top-0 z-20 h-screen w-full bg-black opacity-80"
      />
    )
  );
};

export default TutorialOverlay;
