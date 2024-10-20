export interface ProblemInfo {
  id: number;
  member: Member;
  problem: Problem;
  examInformation: null;
  chosenAnswer: string | null;
  isCorrect: null;
}

export interface IncorrectAnswers {
  id: number;
  member: Member;
  problem: Problem;
  examInformation: null;
  chosenAnswer: string;
  incorrectTime: string;
}

export interface Problem {
  id: number;
  entry: string;
  category: string;
  chosenAnswer: string;
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
