"use client";

import Text from "../ui/Text";
import { useRouter } from "next/navigation";

interface QuestionProps {
  isFinished: boolean;
  total?: number;
  correct?: number;
  solved?: number;
  name?: string;
  params: {
    subject: string;
    type: string;
  };
}

const SectionalResultHeader: React.FC<QuestionProps> = ({
  isFinished,
  params,
}) => {
  const router = useRouter();
  const { subject, type } = params;

  if (isFinished == false) {
    return (
      <>
        <Text size="head-02" className="self-start">
          #이지은님은 #ㅇㅇㅇ유형 10문제 중
        </Text>
        <Text size="head-02" className="self-start">
          #2개의 학습을 완료했어요
        </Text>
        <Text
          size="head-05"
          color="text-gray01"
          className="mb-6 mt-2 self-start"
        >
          남은 문제를 풀고 #ㅇㅇ유형을 정복해보세요
        </Text>
      </>
    );
  }
  if (isFinished == true) {
    return (
      <>
        <Text size="head-02" className="self-start">
          #이지은님은 #ㅇㅇㅇ유형
        </Text>
        <Text size="head-02" className="self-start">
          총 #10문제 중 #5문제를 맞췄어요!
        </Text>
      </>
    );
  }
};

export default SectionalResultHeader;
