"use client";

import { Dispatch, SetStateAction } from "react";
import Text from "@/component/ui/Text";

interface TutorialStep1Props {
  problemId: string;
  step: number;
}

export const TutorialStep1 = ({ problemId, step }: TutorialStep1Props) => {
  if (problemId !== "1") return;
  if (step != 1) return;
  return (
    <>
      <Text
        size="body-02"
        color="text-white"
        className="absolute left-20 top-14 z-20"
      >
        몇 분 남았는 지 확인할 수 있어요!
      </Text>
    </>
  );
};

export const TutorialStep1SpeechBubble = ({ step }: TutorialStep1Props) => {
  if (step != 1) return;
  return (
    <div className="relative top-10 z-20 mr-5 flex w-fit items-center gap-3 self-end rounded-[0.4em] bg-blue03 px-5 py-3 text-blue01 after:absolute after:right-1 after:top-1/2 after:-mr-[15px] after:-mt-[15px] after:h-0 after:w-0 after:border-b-[15px] after:border-l-[15px] after:border-r-0 after:border-t-[15px] after:border-b-transparent after:border-l-blue03 after:border-t-transparent after:content-['']">
      모의고사 풀러가기
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="9"
        height="16"
        viewBox="0 0 9 16"
        fill="none"
      >
        <path
          fillRule="evenodd"
          clipRule="evenodd"
          d="M8.96438 8.16095C8.96734 7.90122 8.86973 7.64055 8.67155 7.44237L7.67512 6.44594L7.66229 6.45878L2.05636 0.852844C1.58594 0.382426 0.823242 0.382426 0.352824 0.852844C-0.117594 1.32326 -0.117595 2.08596 0.352823 2.55638L5.95876 8.16231L0.677413 13.4437C0.206995 13.9141 0.206995 14.6768 0.677413 15.1472C1.14783 15.6176 1.91053 15.6176 2.38095 15.1472L7.66229 9.86584L7.67516 9.87871L8.67158 8.88228C8.87046 8.6834 8.96806 8.42159 8.96438 8.16095Z"
          fill="#4D70EC"
        />
      </svg>
    </div>
  );
};
