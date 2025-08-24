import React, { useState, useEffect } from "react";

const standardColors = [
  "#FF5733",
  "#33FF57",
  "#3357FF",
  "#FF33A1",
  "#FF9133",
  "#FF33F6",
  "#A1FF33",
  "#33FFF2",
  "#F2FF33",
  "#B033FF",
  "#FF8433",
  "#33A1FF",
  "#FFB833",
];

const getColorForInitial = (initial) => {
  const alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  const index = alphabet.indexOf(initial.toUpperCase());
  if (index === -1) return standardColors[0];
  return standardColors[index % standardColors.length];
};

const getInitials = (name) => {
  // Take only the first letter of the name and uppercase it
  return name.trim().charAt(0).toUpperCase();
};

const ProfileIcon = ({ name, size = 50, textColor = "#fff" }) => {
  const [bgColor, setBgColor] = useState("");

  useEffect(() => {
    const firstLetter = getInitials(name);
    const color = getColorForInitial(firstLetter);
    setBgColor(color);
  }, [name]);

  const initials = getInitials(name);

  const iconStyle = {
    width: size,
    height: size,
    borderRadius: "50%",
    backgroundColor: bgColor,
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    fontSize: size / 2.5,
    color: textColor,
    fontWeight: "bold",
    textTransform: "uppercase",
  };

  return bgColor ? (
    <div style={iconStyle}>{initials}</div>
  ) : (
    <div>Loading...</div>
  );
};

export default ProfileIcon;
