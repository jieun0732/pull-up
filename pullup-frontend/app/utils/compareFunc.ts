export function compareScores(average: number, myscore: number) {
  const roundedAverage = roundUpScore(average);
  const roundedMyScore = roundUpScore(myscore);

  if (roundedMyScore > roundedAverage) {
    return "higher";
  } else if (roundedMyScore === roundedAverage) {
    return "same";
  } else {
    return "lower";
  }
}

export function compareTime(average: number, mytime: number) {
  if (mytime > average) {
    return "lower";
  } else if (mytime === average) {
    return "same";
  } else {
    return "higher";
  }
}

export function roundUpScore(num: number): number {
  return Math.round(num * 10) / 10;
}
