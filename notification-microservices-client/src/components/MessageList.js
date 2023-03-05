import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import { useEffect, useState } from "react";
import AddNewMessage from "./forms/AddNewMessage";


const columns = [
  {
    field: "emailUuid",
    headerName: "ID",
    width: 160,
  },
  {
    field: "body",
    headerName: "Description",
    width: 700,
  },
  {
    field: "sendDate",
    headerName: "Deadline",
    width: 180,
  },
  {
    field: "status",
    headerName: "Status",
    width: 150,
  }
];



export default function MessageList() {
    
  const [messages, setMessages] = useState([]);

  useEffect(() => {
      fetch("http://localhost:8080/msg/api/messages")
      .then(response => response.json())
      .then(data => setMessages(data));
  }, []);

  return (
    <div>
    <div style={{
        height: 400,
        width: "65%",
        marginLeft: "auto",
        marginRight: "auto",
        marginTop: "30px"
        }}>
      <DataGrid getRowId={(row) =>  row.emailUuid}
      selectionModel={[]} 
      pageSize={messages.length}
      rowsPerPageOptions={[]}
      rows={messages}
      columns={columns} />
    </div>
    <AddNewMessage/>
    </div>
  );
}
