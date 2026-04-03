import { useState } from "react";
import "./App.css";

function App() {
  const [tab, setTab] = useState("student");

  const [name, setName] = useState("");
  const [cgpa, setCgpa] = useState("");
  const [skills, setSkills] = useState("");
  const [email, setEmail] = useState("");
  const [contact, setContact] = useState("");

  const [search, setSearch] = useState("");
  const [minCgpa, setMinCgpa] = useState("");
  const [students, setStudents] = useState("");

  const register = async () => {
    if (!name || !cgpa || !skills || !email || !contact) {
      alert("Please fill all fields");
      return;
    }

    await fetch("http://localhost:8080/register", {
      method: "POST",
      body: `${name},${cgpa},${skills},${email},${contact}`,
    });

    alert("Student Registered!");

    // clear fields
    setName("");
    setCgpa("");
    setSkills("");
    setEmail("");
    setContact("");
  };

  const searchStudents = async () => {
    try {
      const res = await fetch(
        `http://localhost:8080/search?skill=${search || ""}&cgpa=${minCgpa || 0}`,
      );

      const data = await res.text();
      setStudents(data);
    } catch (err) {
      console.log(err);
      alert("Error fetching data");
    }
  };

  return (
    <div className="container">
      <h1>Training & Placement Portal</h1>

      <div className="tabs">
        <button onClick={() => setTab("student")}>Student</button>
        <button onClick={() => setTab("company")}>Company</button>
      </div>

      {tab === "student" && (
        <div>
          <h2>Student Registration</h2>

          <input
            value={name}
            placeholder="Name"
            onChange={(e) => setName(e.target.value)}
          />
          <br />
          <input
            value={cgpa}
            placeholder="CGPA"
            type="number"
            onChange={(e) => setCgpa(e.target.value)}
          />
          <br />
          <input
            value={skills}
            placeholder="Skills"
            onChange={(e) => setSkills(e.target.value)}
          />
          <br />
          <input
            value={email}
            placeholder="Email"
            onChange={(e) => setEmail(e.target.value)}
          />
          <br />
          <input
            value={contact}
            placeholder="Contact Number"
            onChange={(e) => setContact(e.target.value)}
          />
          <br />

          <button onClick={register}>Register</button>
        </div>
      )}

      {tab === "company" && (
        <div>
          <h2>Company Search</h2>

          <input
            placeholder="Search by name or skill"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <br />

          <input
            placeholder="Minimum CGPA"
            type="number"
            value={minCgpa}
            onChange={(e) => setMinCgpa(e.target.value)}
          />
          <br />

          <button
            onClick={searchStudents}
            disabled={!search.trim() && !minCgpa}
          >
            Search
          </button>

          <pre>{students}</pre>
        </div>
      )}
    </div>
  );
}

export default App;
