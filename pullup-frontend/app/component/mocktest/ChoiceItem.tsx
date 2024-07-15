"use client ";

interface ChoiceItemProps {
  id: number;
  name: string;
  isSelected: boolean;
  onSelect: (id: number) => void;
}

const ChoiceItem: React.FC<ChoiceItemProps> = ({
  id,
  name,
  isSelected,
  onSelect,
}) => {
  return (
    <div
      className={`flex w-full items-center gap-4 px-5 py-4 ${
        isSelected ? "bg-[#ebebeb]" : "bg-white"
      }`}
      onClick={() => onSelect(id)}
    >
      <div
        className={`flex h-10 w-10 items-center justify-center rounded-full border border-solid border-black01 ${
          isSelected ? "bg-black01 text-white" : "bg-white text-black01"
        }`}
      >
        {id}
      </div>
      <div>{name}</div>
    </div>
  );
};

export default ChoiceItem;
