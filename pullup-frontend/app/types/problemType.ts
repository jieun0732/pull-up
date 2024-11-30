export type Entry = "LANGUAGE" | "REASONING" | "MATH" | "SPATIAL";

export interface SolvedProblems {
  entry: Entry;
  evenlyExamInfo: EvenlyExamInfo;
  problemTypeCount: number;
  problemTypeExamInfos: ProblemTypeExamInfo[];
}

interface ProblemTypeExamInfo {
  examId: number;
  isStarted: boolean;
  problemType: string;
  solvedProblemCount: number;
  correctProblemCount: number;
  incorrectProblemCount: number;
  totalProblemCount: number;
}

interface EvenlyExamInfo {
  examId: number;
  isStarted: boolean;
  isFinished: boolean;
  lastSolvedProblemNumber: number;
}

export interface ProblemInfo {
  id: number;
  member: Member;
  problem: Problem;
  examInformation: null;
  chosenAnswer: string | null;
  isCorrect: null;
}

export interface IncorrectProblems {
  list: List[];
}

interface List {
  examId: number;
  entry: string;
  examType: string;
  problemNumber: number;
  solvedDate: string;
  questionSubstring: string;
}

export interface Problem {
  id: number;
  entry: Entry;
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

export interface IncorrectProblem {
  answerId: number;
  problemNumber: number;
  entry: string;
  category: string;
  type: string;
  question: string;
  explanation: string;
  choices: string[];
  chosenAnswer: string;
  correctAnswer: string;
  answerExplain: string;
  incorrectRate: number;
}
