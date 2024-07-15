interface ChoiceItemProps {
  item: { id: number; name: string };
  isSelected: boolean;
  selectedId: string | null;
  setSelectedId: (id: string) => void;
}

const ChoiceItem = ({
  item,
  isSelected,
  selectedId,
  setSelectedId,
}: ChoiceItemProps) => {
  const handleClick = () => {
    if (selectedId === String(item.id)) {
      setSelectedId("");
    } else {
      setSelectedId(String(item.id));
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
        {item.id}
      </div>
      <div>{item.name}</div>
    </div>
  );
};

export default ChoiceItem;
