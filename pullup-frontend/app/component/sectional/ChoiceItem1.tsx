import { FormatQuestion } from "@/utils/FormatQuestion";
import { API } from "@/lib/API";
import LocalStorage from "@/utils/LocalStorage";
import { ProblemBeingSolved } from "@/types/mockexam/mockexamQuestion";
import { Dispatch, SetStateAction } from "react";
import useExamStore from "@/stores/useExamStore";

interface ChoiceItemProps {
  idx: number;
  choice: string;
  isSelected: boolean;
  selectedId: number;
  setSelectedId?: Dispatch<SetStateAction<number>>;
}

const ChoiceItem1 = ({
  idx,
  choice,
  isSelected,
  selectedId,
  setSelectedId,
}: ChoiceItemProps) => {
  const { updateSelectedAnswer } = useExamStore();

  const handleClick = () => {
    if (setSelectedId) {
      if (idx == selectedId && setSelectedId) {
        setSelectedId(-1);
      } else {
        setSelectedId(idx);
      }
    }
  };

  return (
    <div
      className={`flex w-full items-center gap-4 px-5 py-4 ${
        isSelected ? "bg-[#ebebeb]" : "bg-white"
      }`}
      onClick={handleClick}
    >
      <div
        className={`flex h-10 w-10 items-center justify-center rounded-full border border-solid border-black01 ${
          isSelected ? "bg-black01 text-white" : "bg-white text-black01"
        }`}
      >
        {idx + 1}
      </div>
      <div className="min-h-[40px] min-w-0 flex-1">
        {FormatQuestion(choice)}
      </div>
    </div>
  );
};

export default ChoiceItem1;
