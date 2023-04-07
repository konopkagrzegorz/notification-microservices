import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import { useEffect, useState } from "react";

function generateRandom() {
  var length = 8,
      charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
      retVal = "";
  for (var i = 0, n = charset.length; i < length; ++i) {
      retVal += charset.charAt(Math.floor(Math.random() * n));
  }
  return retVal;
}


const columns = [
  {
    field: "messageId",
    headerName: "ID",
    width: 200,
  },
  {
    field: "from",
    headerName: "From",
    width: 300,
  },
  {
    field: "body",
    headerName: "Body",
    width: 800,
  },
  {
    field: "date",
    headerName: "Sent date",
    width: 180,
  },
];



export default function EmailList() {
    
  const [emails, setEmails] = useState([]);

  useEffect(() => {
      fetch("http://localhost:8080/email/api/list-emails")
      .then(response => response.json())
      .then(data => setEmails(data));
  }, []);

  return (
    <div style={{
        height: 400,
        width: "85%",
        marginLeft: "auto",
        marginRight: "auto",
        marginTop: "30px"
        }}>
      <DataGrid getRowId={(row) =>  row.messageId}
      selectionModel={[]} 
      pageSize={emails.length}
      rowsPerPageOptions={[]}
      rows={emails}
      columns={columns}
       />
    </div>
  );
}
