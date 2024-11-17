package pull_up.domain.problem;

public enum Entry {
    LANGUAGE, REASONING, MATH, SPATIAL;

    public static Entry getEntry(String entry) {
        entry = entry.replaceAll("\\s+", "");
        if (entry.equalsIgnoreCase("language") || entry.equals("언어")) return LANGUAGE;
        if (entry.equalsIgnoreCase("reasoning") || entry.equals("추리")) return REASONING;
        if (entry.equalsIgnoreCase("math") || entry.equals("수리")) return MATH;
        if (entry.equalsIgnoreCase("spatial") || entry.equals("공간지각능력")) return SPATIAL;

        throw new IllegalStateException("not supported entry : " + entry);
    }
}
