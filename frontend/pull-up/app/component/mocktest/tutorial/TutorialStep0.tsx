"use client";

import { Dispatch, SetStateAction } from "react";
import Text from "@/component/ui/Text";
import Image from "next/image";
import pop from "@/assets/defaultImages/pop.png";

interface TutorialStep0Props {
  step: number;
  setStep: Dispatch<SetStateAction<number>>;
}

export const TutorialStep0 = ({ step, setStep }: TutorialStep0Props) => {
  if (step != 0) return;
  return (
    <Image
      src={pop}
      alt="tutorial_moveQuestion"
      className="absolute bottom-0 z-50 w-[calc(100%-32px)]"
      priority
      onClick={() => setStep((prevStep) => prevStep + 1)}
    />
  );
};

interface TutorialStep0TextProps {
  step: number;
}

export const TutorialStep0Text = ({ step }: TutorialStep0TextProps) => {
  if (step != 0) return;
  return (
    <>
      <Text
        size="body-02"
        color="text-white"
        className="absolute -top-7 left-4 z-20"
      >
        화살표를 누르면
      </Text>
      <Text
        size="body-02"
        color="text-white"
        className="absolute -top-0 left-4 z-20"
      >
        쉽게 문제 이동을 할 수 있는 창이 나타나요!
      </Text>
    </>
  );
};
