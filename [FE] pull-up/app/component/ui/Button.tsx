import { ComponentPropsWithoutRef } from "react";
import { ButtonSizes, ButtonStyles } from "../../constants/constants";

interface ButtonProps extends ComponentPropsWithoutRef<"button"> {
  size?: "large" | "medium" | "small" | "xsmall";
  color?:
    | "active"
    | "nonactive"
    | "activeBorder"
    | "activeLight"
    | "activeBlack"
    | "green"
    | "red";
  className?: string;
}

const Button = (props: ButtonProps) => {
  const {
    size = "medium",
    color = "active",
    className = "",
    disabled = false,
    children,
    ...rest
  } = props;

  const styles = (() => {
    if (disabled) {
      return "whitespace-nowrap rounded-md bg-gray03 py-3 text-center text-gray02";
    } else {
      return `${ButtonStyles[color]} ${ButtonSizes[size]} ${className}`;
    }
  })();

  return (
    <button {...rest} className={styles}>
      {children}
    </button>
  );
};

export default Button;
