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
    "whitespace-nowrap bg-green02 text-green01 flex items-center justify-center",
  red: "whitespace-nowrap bg-red02 text-red01 flex items-center justify-center",
};

export const TextStyles = {
  "head-01": "font-bold text-[21px]",
  "head-02": "font-bold text-[19px]",
  "head-03": "font-bold text-[17px]",
  "head-04": "font-semibold text-[15px]",
  "head-05": "font-medium text-[15px]",
  "body-01": "font-semibold text-[17px]",
  "body-02": "font-medium text-[17px]",
  "body-03": "font-medium text-[15px]",
  "body-04": "font-normal text-[15px]",
  "caption-01": "font-normal text-[13px]",
  "caption-02": "font-normal text-[11px]",
};

export const sections: Record<string, string> = {
  language: "언어영역",
  reasoning: "추리영역",
  math: "수리영역",
  spatial: "공간지각영역",
};
