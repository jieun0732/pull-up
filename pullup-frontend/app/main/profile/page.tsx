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

interface AuthIconType {
  [key: string]: StaticImageData;
  kakao_user: StaticImageData;
  apple_user: StaticImageData;
}

const authIcon: AuthIconType = {
  kakao_user: kakaoIcon,
  apple_user: appleIcon,
};

export default function Page() {
  const { data, error } = useSWR<User>(`${API}/members/${localStorage.getItem("memberId")}`, fetcher);

  if (!data) return
  
  return (
    <div className="flex h-full flex-col justify-around bg-gray03 px-5 pb-[91px] pt-14">
      {/* 상단 개인 정보 부분 */}
      <div className="gap flex gap-4">
        <Image
          className="h-16 w-16 rounded-full"
          src={defaultProfileImg}
          alt="Profile Image"
        />

        <div className="flex flex-col justify-center">
          <Text size="body-02">{data?.data.name} 님</Text>
          <div className="flex items-center gap-2">
            <Image
              className="h-4 w-4 rounded-full"
              src={authIcon[data?.data.role]}
              alt="Profile Image"
            />
            <Text size="body-04">{data?.data.email}</Text>
          </div>
        </div>
      </div>

      <MyActivities testScore={data?.data.latestScore} />

      <ProfileMenus />
    </div>
  );
}
