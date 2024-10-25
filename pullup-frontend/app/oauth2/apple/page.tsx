"use client";

import AppleRedirect from "@/component/oauth/AppleRedirect";
import React from "react";

const page = async () => {
  return (
    <div>
      <div>apple loading....</div>
      <AppleRedirect />
    </div>
  );
};

export default page;
