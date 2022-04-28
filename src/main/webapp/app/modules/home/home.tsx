import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      
      <Col md="10">
        <div className="container-fluid">
          <img src='../../../content/images/sprintlylogo.png' className="justify-content-center"></img>
          </div>
        <h1>Welcome, Sprinter!</h1>
        
        {account?.login ? (
          <div className="jumbotron">
            <Alert color="primary">You are logged in as user {account.login}.</Alert>
            <Link to="/map">
          <button className="btn btn-primary justify-content-center">Start a Run</button>
        </Link>
        <Link to="/stats" className="alert-link">
        <button className="btn btn-primary justify-content-center">View Stats</button>
              </Link>
          </div>
        ) : (
            <div className="container-fluid">
          <Link to="/login">
          <button className="btn btn-primary justify-content-center">Sign In</button>
        </Link>
        <Link to="/account/register" className="alert-link">
        <button className="btn btn-primary justify-content-center">Sign Up</button>
              </Link>
              </div>
        )}
      </Col>
  
    </Row>
    
  );
};

export default Home;
