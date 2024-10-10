"use client";


import Image from "next/image";
import Text from "./component/ui/Text";
import { useRouter } from "next/navigation";
import introLogo from "@/assets/logo/introLogo.png";
import LocalStorage from "@/utils/LocalStorage";
import { useEffect } from "react";

export default function Home() {
  const totalPercent = 91;
  const router = useRouter();

  useEffect(() => {
    localStorage.clear()
  }, [])

  return (
    <main className="flex h-full w-full flex-col items-center justify-center bg-white px-5">
      <p className="text-[25px] font-bold text-black01">
        풀업 유저 <span className="text-blue01">{totalPercent}%</span>가
      </p>
      <p className="mb-16 text-[25px] font-bold text-black01">
        대기업 취업에 성공했어요!
      </p>
      <Image src={introLogo} alt="로고" className="mb-12" />
      <Text size="body-04" color="text-gray01" className="mb-12">
        인적성 검사 준비는 풀업에서
      </Text>
      <button
        onClick={() => {
          LocalStorage.setItem("memberId", "1")
          router.push("/main/sectional")
        }}
        className="relative mb-5 flex w-full items-center justify-center rounded-md bg-[#fee500] py-5"
      >
        <svg
          className="absolute left-10"
          xmlns="http://www.w3.org/2000/svg"
          width="24"
          height="23"
          viewBox="0 0 24 23"
          fill="none"
        >
          <path
            fillRule="evenodd"
            clipRule="evenodd"
            d="M12.0001 0C5.37225 0 0 4.15061 0 9.26971C0 12.4534 2.07788 15.26 5.24205 16.9293L3.91072 21.7927C3.7931 22.2224 4.28457 22.5649 4.66197 22.3159L10.4978 18.4643C10.9903 18.5118 11.4908 18.5395 12.0001 18.5395C18.6274 18.5395 24 14.3891 24 9.26971C24 4.15061 18.6274 0 12.0001 0Z"
            fill="black"
          />
        </svg>
        <p className="text-center text-base font-semibold text-black">
          카카오로 로그인
        </p>
      </button>
      <button className="relative flex w-full items-center justify-center rounded-md bg-black py-5">
        <svg
          className="absolute left-10"
          xmlns="http://www.w3.org/2000/svg"
          width="18"
          height="23"
          viewBox="0 0 18 23"
          fill="none"
        >
          <path
            d="M17.3867 17.1771C17.0679 17.9472 16.6905 18.6561 16.2532 19.3078C15.6572 20.1962 15.1691 20.8112 14.793 21.1527C14.21 21.7132 13.5853 22.0003 12.9164 22.0166C12.4362 22.0166 11.8571 21.8738 11.1829 21.584C10.5066 21.2955 9.88505 21.1527 9.31673 21.1527C8.72069 21.1527 8.08144 21.2955 7.39768 21.584C6.71288 21.8738 6.16122 22.0248 5.73944 22.0398C5.09798 22.0683 4.4586 21.7731 3.82039 21.1527C3.41305 20.7812 2.90356 20.1445 2.2932 19.2425C1.63833 18.2792 1.09994 17.1622 0.67816 15.8887C0.226444 14.5132 0 13.1812 0 11.8917C0 10.4145 0.305309 9.14048 0.916837 8.07285C1.39744 7.2153 2.03682 6.53883 2.83705 6.04222C3.63728 5.54562 4.50194 5.29256 5.43309 5.27637C5.94259 5.27637 6.61073 5.44113 7.44102 5.76494C8.26897 6.08984 8.80059 6.25461 9.03367 6.25461C9.20793 6.25461 9.79851 6.06195 10.7997 5.67787C11.7464 5.32168 12.5455 5.17419 13.2001 5.23229C14.9739 5.38195 16.3066 6.11297 17.1928 7.42999C15.6064 8.4349 14.8217 9.84239 14.8373 11.648C14.8516 13.0544 15.3396 14.2247 16.2987 15.154C16.7334 15.5853 17.2188 15.9186 17.7589 16.1554C17.6418 16.5105 17.5182 16.8506 17.3867 17.1771ZM13.3185 0.440959C13.3185 1.54329 12.9333 2.57254 12.1655 3.5252C11.2389 4.65772 10.1181 5.31215 8.90275 5.20889C8.88727 5.07664 8.87829 4.93745 8.87829 4.7912C8.87829 3.73296 9.31894 2.60043 10.1015 1.67444C10.4922 1.20559 10.989 0.81575 11.5916 0.504768C12.1928 0.198425 12.7615 0.0290114 13.2964 0C13.312 0.147365 13.3185 0.294753 13.3185 0.440959Z"
            fill="white"
          />
        </svg>
        <p className="text-base font-semibold text-white">Apple ID로 로그인</p>
      </button>
    </main>
  );
}
