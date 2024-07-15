import { ComponentPropsWithoutRef } from "react";
import { TextStyles } from "@/constants/constants";

interface TypographyProps extends ComponentPropsWithoutRef<"p"> {
  size?:
    | "head-01"
    | "head-02"
    | "head-03"
    | "head-04"
    | "head-05"
    | "body-01"
    | "body-02"
    | "body-03"
    | "body-04"
    | "caption-01"
    | "caption-02";
  color?: string;
  className?: string;
}

const Text = (props: TypographyProps) => {
  const {
    size = "body-01",
    color = "black-01",
    className = "",
    children,
    ...rest
  } = props;

  const styles = `${TextStyles[size]} ${color} ${className}`;

  return (
    <p {...rest} className={styles}>
      {children}
    </p>
  );
};

export default Text;
