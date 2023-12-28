import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "./auth/use-auth";

const Home: React.FC = () => {
  let navigate = useNavigate();
  const {isAuthenticated} = useAuth();

  useEffect(() => {
    if (isAuthenticated) {
      navigate("/pam/missions", {replace: true});
    } else {
      navigate("/login", {replace: true});
    }
  }, [isAuthenticated, navigate]);

  return <div/>;
};

export default Home;
