"use client";

import Image from "next/image";
import { StaticImageData } from "next/image";
import defaultProfileImg from "@/assets/defaultImages/profile.png";
import kakaoIcon from "@/assets/defaultImages/kakao.png";
import appleIcon from "@/assets/defaultImages/apple.png";
import MyActivities from "@/component/profile/myActicities";
import ProfileMenus from "@/component/profile/profileMenus";
import Text from "@/component/ui/Text";
import useSWR from "swr";
import { API, fetcher } from "@/lib/API";
import { User } from "@/types/userType";
import useUserStore from "@/stores/useUserStore";
import Spinner from "@/component/ui/Spinner";

export default function Page() {
  const { user } = useUserStore();
  const { data, error } = useSWR<User>(
    `${API}/members/${user.memberId}`,
    fetcher,
  );

  return (
    <>
      {data ? (
        <div className="flex h-full flex-col justify-around bg-gray03 px-5 pb-[91px] pt-20">
          {/* 상단 개인 정보 부분 */}
          <div className="g`ap flex gap-4">
            <Image
              className="h-16 w-16 rounded-full"
              src={defaultProfileImg}
              alt="Profile Image"
            />

            <div className="flex flex-col justify-center">
              <Text size="body-02">{data.name} 님</Text>
              <div className="flex items-center gap-2">
                <Image
                  className="h-4 w-4 rounded-full"
                  src={data.snsProvider === "KAKAO" ? kakaoIcon : appleIcon}
                  alt="Profile Image"
                />
                <Text size="body-04">
                  {data.email === "CONCEALED_EMAIL"
                    ? "이메일을 제공하지 않았습니다."
                    : data.email}
                </Text>
              </div>
            </div>
          </div>

          <MyActivities
            mockExamSolved={data.mockExamSolved}
            mockExamScore={data.mockExamScore}
          />

          <ProfileMenus />
        </div>
      ) : (
        <Spinner />
      )}
    </>
  );
}
