import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
      colors: {
        blue01: "#4d70ec",
        blue02: "#cdd6f4",
        blue03: "#f2f9ff",
        green01: "#4acabb",
        green02: "#e4e7e5",
        red01: "#f17070",
        red02: "#ffe0e0",
        black01: "#3d4150",
        gray01: "#6f6f6f",
        gray02: "#acacac",
        gray03: "#f2f3f6",
        white01: "#ffffff",
        white02: "#fefefe",
        white03: "#eef0f1",
      },
      animation: {
        fadeInUp: "fadeInUp 0.5s ease-in-out",
        fadeOutDown: "fadeOutDown 0.5s ease-in-out",
      },
      keyframes: {
        fadeInUp: {
          "0%": {
            opacity: "0",
            transform: "translateY(20px)",
          },
          "100%": {
            opacity: "1",
            transform: "translateY(0)",
          },
        },
        fadeOutDown: {
          "0%": {
            opacity: "1",
            transform: "translateY(0)",
          },
          "100%": {
            opacity: "0",
            transform: "translateY(20px)",
          },
        },
      },
    },
  },
  plugins: [],
};
export default config;
