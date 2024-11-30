export interface MockExamReportType {
  examId: number;
  name: string;
  scoreInfo: ScoreInfo;
  durationInfo: DurationInfo;
  vulnerableEntryInfo: VulnerableEntryInfo;
}

interface VulnerableEntryInfo {
  vulnerableEntry: string[];
  incorrectLanguageCount: number;
  incorrectReasoningCount: number;
  incorrectMathCount: number;
  totalLanguageCount: number;
  totalReasoningCount: number;
  totalMathCount: number;
}

interface DurationInfo {
  timeLimit: number;
  averageDurationMinute: number;
  myDurationMinute: number;
}

interface ScoreInfo {
  averageScore: number;
  myScore: number;
  topRate: number;
}
