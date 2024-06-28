"use client";
import { useRouter } from "next/navigation";

export default function Page() {
  const isFinished = false;
  const router = useRouter();

  return (
    <div className="flex h-full flex-col justify-between bg-white px-5 pb-[91px] pt-14">
      {isFinished ? (
        <div>
          <p className="text-[19px] font-bold text-black01">
            모의고사 진단이 완료되었어요!
          </p>
          <p className="mb-[26px] text-base text-gray01">
            결과 리포트로 내가 취약한 파트를 알아봐요.
          </p>
        </div>
      ) : (
        <div>
          <p className="text-[19px] font-bold text-black01">문제를 풀고</p>
          <p className="text-[19px] font-bold text-black01">
            골라서 풀어볼 수 있어요!
          </p>
          <p className="mb-[26px] text-base text-gray01">
            나의 취약한 영역을 공략해 효율적으로 학습해요
          </p>
        </div>
      )}
      <div className="self-center">아이콘</div>
      {isFinished ? (
        <div>
          <p className="mb-3 w-full text-center text-[15px] leading-4 text-blue01">
            ㅇㅇ님이 취약한 파트를 <br></br>알 수 있어요
          </p>
          <button className="mb-11 w-full rounded-md bg-blue01 py-4 text-white">
            모의고사 결과 확인하기
          </button>
        </div>
      ) : (
        <div>
          <p className="mb-3 w-full text-center text-[15px] leading-4 text-blue01">
            총 20문제로<br></br>30분 동안 풀 수 있어요
          </p>
          <button
            className="mb-11 w-full rounded-md bg-blue01 py-4 text-white"
            onClick={() => router.push("/main/mocktest/1")}
          >
            모의고사 풀기
          </button>
        </div>
      )}
    </div>
  );
}
