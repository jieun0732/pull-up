import { FormatQuestion } from "@/utils/FormatQuestion";
interface ChoiceItemProps {
  idx: number;
  choice: string;
  isSelected: boolean;
  selectedId: number | null;
  setSelectedId: (id: number | null) => void;
}

const ChoiceItem = ({
  idx,
  choice,
  isSelected,
  selectedId,
  setSelectedId,
}: ChoiceItemProps) => {
  const handleClick = () => {
    if (selectedId === idx) {
      setSelectedId(null);
    } else {
      setSelectedId(idx);
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

export default ChoiceItem;
