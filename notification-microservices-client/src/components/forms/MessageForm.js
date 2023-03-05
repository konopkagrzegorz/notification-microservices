import React from "react";
import { useFormik } from "formik";
import { useState } from "react";
import styled from "styled-components";
import axios from "axios";

const FormContainer = styled.form`
    display: flex;
    width: 600px;
    height:200px;
    flex-direction: column;
`;

function MessageForm({ onAdd }) {

    const status = ['SENT', 'NOT_SENT'];

    const [newMessage, setNewMessage] = useState(null);

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
            axios.post("http://localhost:8080/msg/api/create",JSON.stringify(sample), { headers} )
            .then(console.log(JSON.stringify(values)))
            .then(response => response.json()).then(data => setNewMessage(data));
          return newMessage;  
        },
        validate: (values) => {
            const errors = {};
            if (!values.body) {
                errors.body = "Required!";
            }

            if (values.status.length === 0) {
                errors.status = "Pick one status";
            }

            return errors;
        }
    });

    return <FormContainer onSubmit={handleSubmit}>
    <label htmlFor="body">Body</label>
    <input type="text" id="body" name="body" value={values.body} onChange={handleChange} />
    <label htmlFor="body">UUID</label>
    <input type="text" id="emailUuid" name="emailUuid" value={values.emailUuid} onChange={handleChange} />
    {errors.name && <span style={{ color: 'red'}}>This field is required</span>}
    <label htmlFor="status"/>
          Select roles:
          <select id="status" value={values.status} multiple={false} onChange={handleChange}>
            <option value={status[0]}>{status[0]}</option>
            <option value={status[1]}>{status[1]}</option>
          </select>
    <label htmlFor="sendDate">Deadline</label>
    <input type="date" placeholder="dd-MM-yyyy" min="1997-01-01" max="2030-12-31" id="sendDate" name="sendDate" value={values.sendDate} onChange={handleChange} />
    <input type="submit" value="Send" />
</FormContainer>

}

export default MessageForm;