import { ComponentPropsWithoutRef } from "react";
import { ButtonSizes, ButtonStyles } from "../../constants/constants";

interface ButtonProps extends ComponentPropsWithoutRef<"button"> {
  size?: "large" | "medium" | "small" | "xsmall";
  color?:
    | "active"
    | "nonactive"
    | "activeBorder"
    | "activeLight"
    | "green"
    | "red";
  customstyle?: string;
}

const Button = (props: ButtonProps) => {
  const {
    size = "medium",
    color = "active",
    customstyle = "",
    disabled = false,
    children,
    ...rest
  } = props;

  const styles = (() => {
    if (disabled) {
      return "whitespace-nowrap rounded-md bg-gray03 py-3 text-center text-gray02";
    } else {
      return `${ButtonStyles[color]} ${ButtonSizes[size]} ${customstyle}`;
    }
  })();

  return (
    <button {...rest} className={styles}>
      {children}
    </button>
  );
};

export default Button;
