import React, { useEffect, useState } from "react";
import axios from "axios";
import { Table, Button, Modal, Form } from "react-bootstrap";
import { useAuth } from "./components/AuthContext.js";


const Dashboard = () => {
  const { username } = useAuth();
  const [urls, setUrls] = useState([]); // Store URLs
  const [showRenameModal, setShowRenameModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedUrl, setSelectedUrl] = useState(null);
  const [newAlias, setNewAlias] = useState("");
  const [newLongUrl, setNewLongUrl] = useState("");

   // Fetch URLs and Analytics
   useEffect(() => {
    if (username) {
      fetchUrls();
    }
  }, [username]);

  const fetchUrls = async () => {
    try {
      const response = await axios.get("/api/urls", {
        headers: {
          Username: username, // Replace with actual username from authentication
        },
      });
      setUrls(response.data);
    } catch (error) {
      console.error("Error fetching URLs:", error);
    }
  };

  // Rename URL 
  const handleRename = async () => {
    try {
      await axios.put(
        `/api/urls/rename`,
        { newAlias },
        {
          headers: {
            Username: username, // Replace with actual username from authentication
          },
        }
      );
      fetchUrls(); // Refresh the table
      setShowRenameModal(false);
    } catch (error) {
      console.error("Error renaming URL:", error);
    }
  };

  // Edit Destination URL
  const handleEdit = async () => {
    try {
      await axios.put(
        `/api/urls/edit`,
        { newLongUrl },
        {
          headers: {
            Username: username, // Replace with actual username from authentication
          },
        }
      );
      fetchUrls(); // Refresh the table
      setShowEditModal(false);
    } catch (error) {
      console.error("Error editing destination URL:", error);
    }
  };

  return (
    <div className="container mt-5">
      <h2>Dashboard</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Shortened URL</th>
            <th>Original Destination</th>
            <th>Click Count</th>
            <th>Created At</th>
            <th>Last Accessed</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {urls.map((url) => (
            <tr key={url.id}>
              <td>
                <a href={url.shortUrl} target="_blank" rel="noopener noreferrer">
                  {url.shortUrl}
                </a>
              </td>
              <td>{url.longUrl}</td>
              <td>{url.clickCount}</td>
              <td>{new Date(url.createdAt).toLocaleString()}</td>
              <td>
                {url.lastAccessed
                  ? new Date(url.lastAccessed).toLocaleString()
                  : "N/A"}
              </td>
              <td>
                <Button
                  variant="warning"
                  className="me-2"
                  onClick={() => {
                    setSelectedUrl(url);
                    setShowRenameModal(true);
                  }}
                >
                  Rename
                </Button>
                <Button
                  variant="primary"
                  onClick={() => {
                    setSelectedUrl(url);
                    setShowEditModal(true);
                  }}
                >
                  Edit
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Rename Modal */}
      <Modal show={showRenameModal} onHide={() => setShowRenameModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Rename Shortened URL</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group>
              <Form.Label>New Alias</Form.Label>
              <Form.Control
                type="text"
                value={newAlias}
                onChange={(e) => setNewAlias(e.target.value)}
                placeholder="Enter new alias"
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowRenameModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleRename}>
            Save
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Edit Modal */}
      <Modal show={showEditModal} onHide={() => setShowEditModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Edit Destination URL</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group>
              <Form.Label>New Destination URL</Form.Label>
              <Form.Control
                type="url"
                value={newLongUrl}
                onChange={(e) => setNewLongUrl(e.target.value)}
                placeholder="Enter new destination URL"
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowEditModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleEdit}>
            Save
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default Dashboard;
