type ItemType = {
  id: number;
  type: string;
  qtype: "nonactive" | "active" | "wrong" | "right";
};

export const dummyresult: ItemType[] = [
  {
    id: 1,
    type: "유의어",
    qtype: "right",
  },
  {
    id: 2,
    type: "유의어",
    qtype: "wrong",
  },
  {
    id: 3,
    type: "유의어",
    qtype: "wrong",
  },
  {
    id: 4,
    type: "유의어",
    qtype: "wrong",
  },
  {
    id: 5,
    type: "유의어",
    qtype: "wrong",
  },
  {
    id: 6,
    type: "유의어",
    qtype: "right",
  },
];
