export interface mockexamQuestionType {
  id: number;
  entry: string;
  category: string;
  type: string;
  question: string;
  explanation: string;
  choices: string[];
  answer: string;
  answerExplain: string;
  totalAttempts: null;
  incorrectAttempts: null;
  incorrectRate: null;
}

export interface ProblemBeingSolved {
  id: number;
  problem: Problem;
  chosenAnswer: string;
  isCorrect: boolean;
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
  answerExplain: string;
  totalAttempts: null;
  incorrectAttempts: null;
  incorrectRate: null;
}
