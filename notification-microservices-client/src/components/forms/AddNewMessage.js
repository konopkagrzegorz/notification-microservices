import React from "react";
import styled from 'styled-components';
import { useState } from 'react';
import Fab from '@mui/material/Fab';
import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import Icon from '@mui/material/Icon';
import MessageForm from './MessageForm';
import AddCircleIcon from '@mui/icons-material/AddCircle';

const AddNewMessageContainer = styled.div`
    margin-top: 14px;
    margin-bottom: 35px;
    margin-right: 40%;
    align-self: flex-end;
`;

const AddNewMessage = ({ onAdd }) => {
    const [isOpen, setIsOpen] = useState(false);

    const handleOpen = () => {
        setIsOpen(true);
    };

    const handleClose = () => {
        setIsOpen(false);
    };

    const handleOnAdd = () => {
        onAdd();
        handleClose();
    }

    return <AddNewMessageContainer>
        <Fab color="primary" onClick={handleOpen}>
            <AddCircleIcon>Add</AddCircleIcon>
        </Fab>
        <Dialog onClose={handleClose} open={isOpen}>
            <DialogTitle>Add new Message</DialogTitle>
            <MessageForm onAdd={handleOnAdd} />
        </Dialog>
    </AddNewMessageContainer>
}
export default AddNewMessage