"use client";

import Spinner from "@/component/ui/Spinner";
import AppleRedirect from "@/component/oauth/AppleRedirect";

const page = async () => {
  return (
    <>
      <Spinner />
      <AppleRedirect />
    </>
  );
};
export default page;
