import React, { useEffect, useState } from "react";
import {
  getAllProjects,
  createProject,
  updateProject,
  deleteProject,
} from "../services/ProjectService";

const emptyForm = {
  name: "",
  description: "",
  managerId: "",
};

function Project() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [formData, setFormData] = useState(emptyForm);
  const [editId, setEditId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [saving, setSaving] = useState(false);
  const [deletingId, setDeletingId] = useState(null);

  useEffect(() => {
    loadProjects();
  }, []);

  const loadProjects = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await getAllProjects();
      setProjects(response.data.data);
    } catch (err) {
      console.error("Error fetching projects:", err);
      setError("Could not load projects. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const resetForm = () => {
    setFormData(emptyForm);
    setEditId(null);
    setShowForm(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editId) {
        await updateProject(editId, formData);
      } else {
        await createProject(formData);
      }
      resetForm();
      loadProjects();
    } catch (err) {
      console.error("Error saving project:", err);
      alert("Could not save project. Please check the details and try again.");
    } finally {
      setSaving(false);
    }
  };

  const handleEdit = (project) => {
    setEditId(project.id);
    setFormData({
      name: project.name || "",
      description: project.description || "",
      managerId: project.managerId ?? "",
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this project? This cannot be undone.")) return;
    setDeletingId(id);
    try {
      await deleteProject(id);
      loadProjects();
      if (editId === id) resetForm();
    } catch (err) {
      console.error("Error deleting project:", err);
      alert("Could not delete project.");
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div className="min-h-screen w-full bg-slate-50 px-4 py-8">
      <div className="max-w-5xl mx-auto">

        <div className="mb-6 flex items-center justify-between">
          <div>
            <h1 className="text-xl font-medium text-slate-900">Projects</h1>
            <p className="text-sm text-slate-500 mt-1">
              {loading ? "Loading..." : `${projects.length} project${projects.length !== 1 ? "s" : ""} found`}
            </p>
          </div>

          {!showForm && (
            <button
              onClick={() => setShowForm(true)}
              className="px-4 py-2.5 rounded-lg bg-blue-600 text-white text-sm font-medium hover:bg-blue-700 active:scale-[0.98] transition"
            >
              + Create project
            </button>
          )}
        </div>

        {showForm && (
          <div className="bg-white rounded-2xl border border-slate-200 p-6 mb-6">
            <h2 className="text-base font-medium text-slate-900 mb-4">
              {editId ? `Edit project #${editId}` : "Create new project"}
            </h2>

            <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">

              <div className="md:col-span-2">
                <label className="block text-xs font-medium text-slate-600 mb-1.5">
                  Project name
                </label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  placeholder="e.g. E-Commerce Platform"
                  className="w-full px-3 py-2.5 text-sm rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder:text-slate-400"
                />
              </div>

              <div className="md:col-span-2">
                <label className="block text-xs font-medium text-slate-600 mb-1.5">
                  Description
                </label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  required
                  rows={2}
                  placeholder="Brief description of the project"
                  className="w-full px-3 py-2.5 text-sm rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder:text-slate-400 resize-none"
                />
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-600 mb-1.5">
                  Manager ID
                </label>
                <input
                  type="text"
                  name="managerId"
                  value={formData.managerId}
                  onChange={handleChange}
                  required
                  placeholder="e.g. 2"
                  className="w-full px-3 py-2.5 text-sm rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder:text-slate-400"
                />
              </div>

              <div className="md:col-span-2 flex items-center gap-3 mt-1">
                <button
                  type="submit"
                  disabled={saving}
                  className="px-4 py-2.5 rounded-lg bg-blue-600 text-white text-sm font-medium hover:bg-blue-700 active:scale-[0.98] transition disabled:opacity-60 disabled:cursor-not-allowed"
                >
                  {saving ? "Saving..." : editId ? "Update project" : "Create project"}
                </button>

                <button
                  type="button"
                  onClick={resetForm}
                  className="px-4 py-2.5 rounded-lg border border-slate-200 text-sm font-medium text-slate-600 hover:bg-slate-50 transition"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {loading && (
          <div className="bg-white rounded-2xl border border-slate-200 p-10 flex flex-col items-center justify-center">
            <div className="w-6 h-6 border-2 border-slate-200 border-t-blue-600 rounded-full animate-spin mb-3"></div>
            <p className="text-sm text-slate-500">Fetching projects...</p>
          </div>
        )}

        {!loading && error && (
          <div className="bg-red-50 border border-red-100 rounded-2xl p-6 text-center">
            <p className="text-sm text-red-600">{error}</p>
            <button
              onClick={loadProjects}
              className="mt-3 text-sm font-medium text-red-700 underline"
            >
              Try again
            </button>
          </div>
        )}

        {!loading && !error && projects.length === 0 && (
          <div className="bg-white rounded-2xl border border-slate-200 p-10 text-center">
            <p className="text-sm text-slate-500">No projects found. Create one above.</p>
          </div>
        )}

        {!loading && !error && projects.length > 0 && (
          <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
            <table className="w-full text-sm">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200">
                  <th className="text-left font-medium text-slate-500 px-5 py-3">ID</th>
                  <th className="text-left font-medium text-slate-500 px-5 py-3">Project name</th>
                  <th className="text-left font-medium text-slate-500 px-5 py-3">Description</th>
                  <th className="text-left font-medium text-slate-500 px-5 py-3">Manager ID</th>
                  <th className="text-right font-medium text-slate-500 px-5 py-3">Actions</th>
                </tr>
              </thead>
              <tbody>
                {projects.map((project, index) => (
                  <tr
                    key={project.id}
                    className={`border-b border-slate-100 hover:bg-slate-50 transition ${
                      index === projects.length - 1 ? "border-b-0" : ""
                    } ${editId === project.id ? "bg-blue-50/40" : ""}`}
                  >
                    <td className="px-5 py-3 text-slate-500">#{project.id}</td>
                    <td className="px-5 py-3 font-medium text-slate-900">{project.name}</td>
                    <td className="px-5 py-3 text-slate-600 max-w-xs truncate">
                      {project.description || "—"}
                    </td>
                    <td className="px-5 py-3 text-slate-600">{project.managerId}</td>
                    <td className="px-5 py-3 text-right">
                      <button
                        onClick={() => handleEdit(project)}
                        className="text-blue-600 text-xs font-medium hover:underline mr-3"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDelete(project.id)}
                        disabled={deletingId === project.id}
                        className="text-red-600 text-xs font-medium hover:underline disabled:opacity-50"
                      >
                        {deletingId === project.id ? "Deleting..." : "Delete"}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

      </div>
    </div>
  );
}

export default Project;