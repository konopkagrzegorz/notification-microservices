import React from "react";
import { useFormik } from "formik";
import { useState } from "react";
import styled from "styled-components";
import axios from "axios";
import e from "cors";
import "./MessageForm.css";

const FormContainer = styled.form`
    display: flex;
    flex-direction: column;
`;

const SuccessMessage = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 600px;
  height: 50px;
  background-color: #d7f7e6;
  color: #22bb33;
  padding: 10px;
`;

const CloseButton = styled.button`
  position: absolute;
  top: 0;
  right: 0;
  font-size: 24px;
  line-height: 24px;
  padding: 8px;
  border: none;
  background: none;
  cursor: pointer;
  color: #999;
  &:hover {
    color: #333;
  }
`;


function MessageForm({ onAdd }) {

    const status = ['SENT', 'NOT_SENT'];

    const [newMessage, setNewMessage] = useState(null);
    const [submitted, setSubmitted] = useState(false);

    const headers = {
        'Content-Type': 'application/json'
    }

    const sample = {
        body: "body",
        emailUuid: "1232323",
        status: "NOT_SENT",
        sendDate: "16-02-2022"
    }

    const {values, errors, handleSubmit, handleChange} = useFormik({
        initialValues: {
            body: '',
            emailUuid: '',
            status: 'NOT_SENT',
            sendDate: ''
        },
        onSubmit: (values) => {
            var dateToRevert = values.sendDate.split("-");
            var parsedDate = dateToRevert[2] + "-" + dateToRevert[1] + "-" + dateToRevert[0];

            const parsedMessage = {
                body: values.body,
                emailUuid: values.emailUuid,
                status: values.status,
                sendDate: parsedDate
            };
            setSubmitted(true);
            return axios.post("http://localhost:8083/msg/api/create", JSON.stringify(parsedMessage), { headers } )
            .then(response => response.json()).then(data => setNewMessage(data));
        },

        validate: (values) => {
            const errors = {};
            if (!values.body) {
                errors.body = "Body is required!";
            }

            if (!values.emailUuid) {
                errors.emailUuid = "UUID is required!";
            }

            if (!values.sendDate) {
                errors.sendDate = "Please pick a date!";
            }

            if (values.status.length === 0) {
                errors.status = "Pick one status";
            }

            return errors;
        }
    });

    if (!submitted) {
        return <FormContainer onSubmit={handleSubmit}>
        <label htmlFor="body">Body</label>
        <input type="text" id="body" name="body" value={values.body} onChange={handleChange} />
        {errors.body && <div style={{ color: 'red' }}>{errors.body}</div>}
        
        <label htmlFor="emailUuid">UUID</label>
        <input type="text" id="emailUuid" name="emailUuid" value={values.emailUuid} onChange={handleChange} />
        {errors.emailUuid && <div style={{ color: 'red' }}>{errors.emailUuid}</div>}
        
        <label htmlFor="status"/>
        Select roles:
        <select id="status" value={values.status} multiple={false} onChange={handleChange}>
            <option value={status[0]}>{status[0]}</option>
            <option value={status[1]}>{status[1]}</option>
        </select>
        {errors.status && <div style={{ color: 'red' }}>{errors.status}</div>}
        
        <label htmlFor="sendDate">Deadline</label>
        <input type="date" placeholder="dd-MM-yyyy" min="1997-01-01" max="2030-12-31" id="sendDate" name="sendDate" value={values.sendDate} onChange={handleChange} />
        {errors.sendDate && <div style={{ color: 'red' }}>{errors.sendDate}</div>}
        
        <input type="submit" value="Send" />
    </FormContainer>;
    }
        return (
          <SuccessMessage>
            <span>Message successfully sent!</span>
            <CloseButton onClick={() => setSubmitted(false)}>Close</CloseButton>
          </SuccessMessage>
        );
}

export default MessageForm;