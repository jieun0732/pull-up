import Image from "next/image";
import { StaticImageData } from "next/image";
import defaultProfileImg from "@/assets/defaultImages/profile.png";
import kakaoIcon from "@/assets/defaultImages/kakao.png";
import appleIcon from "@/assets/defaultImages/apple.png";
import MyActivities from "@/component/profile/myActicities";
import ProfileMenus from "@/component/profile/profileMenus";

interface AuthIconType {
  [key: string]: StaticImageData;
  kakao: StaticImageData;
  apple: StaticImageData;
}

const authIcon: AuthIconType = {
  kakao: kakaoIcon,
  apple: appleIcon,
};

export default function Page() {
  const dummydata = {
    name: "이지은",
    email: "abc@naver.com",
    auth: "kakao",
    testScore: "85",
  };

  return (
    <div className="flex h-full flex-col justify-around overflow-x-auto bg-gray03 px-5 pb-[91px] pt-14">
      {/* 상단 개인 정보 부분 */}
      <div className="gap flex gap-4">
        <Image
          className="h-16 w-16 rounded-full"
          src={defaultProfileImg}
          alt="Profile Image"
        />

        <div className="flex flex-col justify-center">
          <p className="font-lg font-semibold">{dummydata.name} 님</p>
          <div className="flex items-center gap-2">
            <Image
              className="h-4 w-4 rounded-full"
              src={authIcon[dummydata.auth]}
              alt="Profile Image"
            />
            <p className="font-normal">{dummydata.email}</p>
          </div>
        </div>
      </div>

      <MyActivities testScore={dummydata.testScore} />

      <ProfileMenus />
    </div>
  );
}
