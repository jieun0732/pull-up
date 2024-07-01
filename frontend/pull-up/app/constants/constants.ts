export const ButtonSizes = {
  xsmall: 22,
  small: "px-2 py-1 rounded-sm text-[13px]",
  medium: "w-[50%] rounded-md py-3",
  large: "w-full rounded-md font-semibold text-base py-3",
};

export const ButtonStyles = {
  active:
    "whitespace-nowrap bg-blue01 text-white flex items-center justify-center",
  nonactive:
    "whitespace-nowrap bg-gray03 text-center text-gray02 flex items-center justify-center",
  activeBorder:
    "whitespace-nowrap border-2 border-solid border-blue01 bg-white text-blue01 flex items-center justify-center",
  activeLight:
    "whitespace-nowrap bg-blue03 text-blue01 flex items-center justify-center",
  activeBlack:
    "whitespace-nowrap bg-gray03 text-black01 flex items-center justify-center",
  green:
    "group flex w-full items-center justify-center rounded-md border border-solid border-black bg-white text-black hover:bg-black/10 active:bg-black active:text-white",
  red: "group flex w-full items-center justify-center rounded-md border border-solid border-black bg-white text-black hover:bg-black/10 active:bg-black active:text-white",
};

export const sections: Record<string, string> = {
  language: "언어영역",
  reasoning: "추리영역",
  math: "수리영역",
  spatial: "공간지각영역",
};
