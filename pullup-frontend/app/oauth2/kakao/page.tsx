"use client";
import Spinner from "@/component/ui/Spinner";
import KakaoRedirect from "@/component/oauth/KakaoRedirect";

const page = async () => {
  return (
    <div>
      <Spinner />
      <KakaoRedirect />
    </div>
  );
};
export default page;
