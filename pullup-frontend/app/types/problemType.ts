export interface ProblemInfo {
  id: number;
  member: Member;
  problem: Problem;
  examInformation: null;
  chosenAnswer: null;
  isCorrect: null;
}

export interface Problem {
  id: number;
  entry: string;
  category: string;
  type: string;
  question: string;
  explanation: string;
  choices: string[];
  answer: string;
  answerExplain: null;
  totalAttempts: null;
  incorrectAttempts: null;
  incorrectRate: null;
}

export interface Member {
  id: number;
  name: string;
  email: string;
  accessCheck: boolean;
  role: string;
}
