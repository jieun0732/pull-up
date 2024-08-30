"use client";

import Header from "@/component/ui/Header";
import { dummywrong } from "@/constants/dummywrong";
import Button from "@/component/ui/Button";
import { entryMap } from "@/constants/constants";
import Text from "@/component/ui/Text";

export default function Page() {
  return (
    <div className="flex h-full w-full flex-col items-center bg-[#F4F3F8] px-5 pb-[91px] pt-14">
      <Header type="back" content="내가 틀린 문제" link="/main/profile" />
      <div className="flex h-full w-full flex-col items-center justify-center overflow-x-auto">
        {dummywrong.length === 0 ? (
          <p>아직 푼 문제가 없어요!</p>
        ) : (
          dummywrong.map((item) => {
            return (
              <div
                key={item.id}
                className="mb-4 w-full rounded-lg bg-white px-4 py-5"
              >
                <div className="mb-4 flex w-full items-center gap-[7px]">
                  <Button size="small" color="activeLight">
                    {entryMap[item.section]}
                  </Button>
                  <Button size="small" color="nonactive">
                    골고루 학습
                  </Button>
                  <Button size="small" color="activeLight">
                    {item.questionNum}번
                  </Button>
                  <Text
                    size="caption-01"
                    color="text-gray02"
                    className="ml-auto"
                  >
                    {item.date}
                  </Text>
                </div>
                <div className="line-clamp-2 w-full overflow-hidden text-ellipsis whitespace-normal">
                  {item.preview}
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}
