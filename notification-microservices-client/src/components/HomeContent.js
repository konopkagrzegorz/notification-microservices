import { Card } from "@mui/material";
import { Typography } from "@mui/material";
import styled from "styled-components";

const HomeContainer = styled.div`
    margin-left:auto;
    margin-right:auto;
    text-align:center;
    border: 2px solid, grey;
    border-radius: 20px;
    margin-top: 36px;
    width: 75%
`;

export const HomeContent = () => {
  return (
    <>
      <HomeContainer>
        <Card variant="outlined" style={{ backgroundColor: "#DCDCDC" }}>
          <Typography variant="h3" sx={{ flexGrow: 1 }}>
            Welcome to notification-microservices app
          </Typography>
        </Card>
      </HomeContainer>
    </>
  );
};
