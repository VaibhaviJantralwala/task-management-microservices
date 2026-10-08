import React, { useEffect, useState } from "react";
import {
  getAllTasks,
  createTask,
  updateTask,
  deleteTask,
  updateTaskStatus,
} from "../services/TaskService";
import { getAllProjects } from "../services/ProjectService";
import { getAllUsers } from "../services/UserService";

const statusOptions = ["TODO", "IN_PROGRESS", "DONE"];

const columnConfig = {
  TODO: { label: "To Do", dot: "bg-slate-400" },
  IN_PROGRESS: { label: "In Progress", dot: "bg-blue-400" },
  DONE: { label: "Done", dot: "bg-green-400" },
};

const priorityStyles = {
  HIGH: "bg-red-50 text-red-600",
  MEDIUM: "bg-amber-50 text-amber-600",
  LOW: "bg-slate-100 text-slate-500",
};

const emptyForm = {
  taskName: "",
  description: "",
  status: "TODO",
  projectId: "",
  assignedUserId: "",
};

const Task = () => {
  const [tasks, setTasks] = useState([]);
  const [projects, setProjects] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [saving, setSaving] = useState(false);
  const [deletingId, setDeletingId] = useState(null);
  const [showForm, setShowForm] = useState(false);

  const [draggedTaskId, setDraggedTaskId] = useState(null);
  const [dragOverColumn, setDragOverColumn] = useState(null);
  const [selectedProjectId, setSelectedProjectId] = useState("");
  const filteredTasks = selectedProjectId
    ? tasks.filter((t) => t.projectId === selectedProjectId)
    : tasks;


  useEffect(() => {
    fetchAll();
  }, []);

  const fetchAll = async () => {
    setLoading(true);
    setError(null);
    try {
      const [tasksRes, projectsRes, usersRes] = await Promise.all([
        getAllTasks(),
        getAllProjects(),
        getAllUsers(),
      ]);
      setTasks(tasksRes.data.data);
      setProjects(projectsRes.data.data);
      setUsers(usersRes.data.data);
    } catch (err) {
      console.error("Error loading data:", err);
      setError("Could not load data. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const resetForm = () => {
    setForm(emptyForm);
    setEditingId(null);
    setShowForm(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      if (editingId) {
        await updateTask(editingId, form);
      } else {
        await createTask(form);
      }
      resetForm();
      const res = await getAllTasks();
      setTasks(res.data.data);
    } catch (err) {
      console.error("Error saving task:", err);
      alert("Could not save task?. Please check the details and try again.");
    } finally {
      setSaving(false);
    }
  };

  const handleEditClick = (task) => {
    
    setEditingId(task?.id);
    setForm({
      taskName: task?.taskName || "",
      description: task?.description || "",
      status: task?.status || "TODO",
      projectId: task?.projectId ?? "",
      assignedUserId: task?.assignedUserId ?? "",
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Delete this task? This cannot be undone.")) return;
    setDeletingId(id);
    try {
      await deleteTask(id);
      const res = await getAllTasks();
      setTasks(res.data.data);
      if (editingId === id) resetForm();
    } catch (err) {
      console.error("Error deleting task:", err);
      alert("Could not delete task?.");
    } finally {
      setDeletingId(null);
    }
  };

  const handleDragStart = (taskId) => setDraggedTaskId(taskId);
  const handleDragEnd = () => {
    setDraggedTaskId(null);
    setDragOverColumn(null);
  };
  const handleDragOver = (e, columnStatus) => {
    e.preventDefault();
    setDragOverColumn(columnStatus);
  };

  const handleDrop = async (e, newStatus) => {
    e.preventDefault();
    setDragOverColumn(null);
    if (!draggedTaskId) return;

    const task = tasks.find((t) => t.id === draggedTaskId);
    if (!task || task?.status === newStatus) {
      setDraggedTaskId(null);
      return;
    }

    setTasks((prev) =>
      prev.map((t) => (t.id === draggedTaskId ? { ...t, status: newStatus } : t))
    );

    try {
      await updateTaskStatus(draggedTaskId, newStatus);
    } catch (err) {
      console.error("Error updating status:", err);
      alert("Could not move task?. Reverting.");
      setTasks((prev) =>
        prev.map((t) => (t.id === draggedTaskId ? { ...t, status: task?.status } : t))
      );
    } finally {
      setDraggedTaskId(null);
    }
  };

  const getInitials = (name) => {
    if (!name) return "?";
    return name.trim().charAt(0).toUpperCase();
  };

  // Project name dhundho ID se — card mein dikhane ke liye
  const getProjectName = (projectId) => {
    const p = projects.find((p) => p.id === projectId );
    return p ? p.name : null;
  };

  // User name dhundho ID se — card mein dikhane ke liye
  const getUserName = (userId) => {
    const u = users.find((u) => u.id === userId || u.id === Number(userId));
    return u ? (u.username || u.name || u.email) : null;
  };

  const selectClass =
    "w-full px-3 py-2.5 text-sm rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent bg-white";

  return (
    <div className="min-h-screen w-full bg-slate-50 px-4 py-8">
      <div className="max-w-6xl mx-auto">

        {/* Header */}
        <div className="mb-6 flex items-center justify-between gap-4">
          <div>
            <h1 className="text-xl font-medium text-slate-900">Tasks</h1>
            <p className="text-sm text-slate-500 mt-1">
              {loading
                ? "Loading..."
                : `${filteredTasks.length} task${filteredTasks.length !== 1 ? "s" : ""} found`}
            </p>
          </div>

          <div className="flex items-center gap-3">
            {/* ✅ Project Filter Dropdown */}
            <select
              value={selectedProjectId}
              onChange={(e) => setSelectedProjectId(e.target.value)}
              className="px-3 py-2.5 text-sm rounded-lg border border-slate-200 bg-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent text-slate-700 min-w-[180px]"
            >
              <option value="">All projects</option>
              {projects.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.name}
                </option>
              ))}
            </select>

            {!showForm && (
              <button
                onClick={() => setShowForm(true)}
                className="px-4 py-2.5 rounded-lg bg-blue-600 text-white text-sm font-medium hover:bg-blue-700 active:scale-[0.98] transition"
              >
                + Create task
              </button>
            )}
          </div>
        </div>

        {/* Create / Edit Form */}
        {showForm && (
          <div className="bg-white rounded-2xl border border-slate-200 p-6 mb-6">
            <h2 className="text-base font-medium text-slate-900 mb-4">
              {editingId ? `Edit task #${editingId}` : "Create new task"}
            </h2>

            <form
              onSubmit={handleSubmit}
              className="grid grid-cols-1 md:grid-cols-2 gap-4"
            >
              {/* Task name */}
              <div className="md:col-span-2">
                <label className="block text-xs font-medium text-slate-600 mb-1.5">
                  Task name
                </label>
                <input
                  type="text"
                  name="taskName"
                  value={form.taskName}
                  onChange={handleChange}
                  required
                  placeholder="e.g. Create login API"
                  className="w-full px-3 py-2.5 text-sm rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder:text-slate-400"
                />
              </div>

              {/* Description */}
              <div className="md:col-span-2">
                <label className="block text-xs font-medium text-slate-600 mb-1.5">
                  Description
                </label>
                <textarea
                  name="description"
                  value={form.description}
                  onChange={handleChange}
                  rows={2}
                  placeholder="Brief description of the task"
                  className="w-full px-3 py-2.5 text-sm rounded-lg border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder:text-slate-400 resize-none"
                />
              </div>

              {/* Status */}
              <div>
                <label className="block text-xs font-medium text-slate-600 mb-1.5">
                  Status
                </label>
                <select
                  name="status"
                  value={form.status}
                  onChange={handleChange}
                  className={selectClass}
                >
                  {statusOptions.map((s) => (
                    <option key={s} value={s}>
                      {columnConfig[s].label}
                    </option>
                  ))}
                </select>
              </div>

              {/* Project dropdown — naam dikhega, ID jaayega */}
              <div>
                <label className="block text-xs font-medium text-slate-600 mb-1.5">
                  Project
                </label>
                <select
                  name="projectId"
                  value={form.projectId}
                  onChange={handleChange}
                  required
                  className={selectClass}
                >
                  <option value="">— Select project —</option>
                  {projects.map((p) => (
                    <option key={p.id} value={p.id}>
                      {p.name}
                    </option>
                  ))}
                </select>
              </div>

              {/* User dropdown — username dikhega, ID jaayega */}
              <div>
                <label className="block text-xs font-medium text-slate-600 mb-1.5">
                  Assign to
                </label>
                <select
                  name="assignedUserId"
                  value={form.assignedUserId}
                  onChange={handleChange}
                  required
                  className={selectClass}
                >
                  <option value="">— Select user —</option>
                  {users.map((u) => (
                    <option key={u.id} value={u.id}>
                      {u.username || u.name || u.email || `User #${u.id}`}
                    </option>
                  ))}
                </select>
              </div>

              {/* Buttons */}
              <div className="md:col-span-2 flex items-center gap-3 mt-1">
                <button
                  type="submit"
                  disabled={saving}
                  className="px-4 py-2.5 rounded-lg bg-blue-600 text-white text-sm font-medium hover:bg-blue-700 active:scale-[0.98] transition disabled:opacity-60 disabled:cursor-not-allowed"
                >
                  {saving
                    ? "Saving..."
                    : editingId
                      ? "Update task"
                      : "Create task"}
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

        {/* Loading */}
        {loading && (
          <div className="bg-white rounded-2xl border border-slate-200 p-10 flex flex-col items-center justify-center">
            <div className="w-6 h-6 border-2 border-slate-200 border-t-blue-600 rounded-full animate-spin mb-3" />
            <p className="text-sm text-slate-500">Loading...</p>
          </div>
        )}

        {/* Error */}
        {!loading && error && (
          <div className="bg-red-50 border border-red-100 rounded-2xl p-6 text-center">
            <p className="text-sm text-red-600">{error}</p>
            <button
              onClick={fetchAll}
              className="mt-3 text-sm font-medium text-red-700 underline"
            >
              Try again
            </button>
          </div>
        )}

        {/* Kanban Board */}
        {!loading && !error && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
            {statusOptions.map((statusKey) => {
              const columnTasks = filteredTasks.filter((t) => t.status === statusKey);

              const isDragOver = dragOverColumn === statusKey;

              return (
                <div
                  key={statusKey}
                  onDragOver={(e) => handleDragOver(e, statusKey)}
                  onDragLeave={() => setDragOverColumn(null)}
                  onDrop={(e) => handleDrop(e, statusKey)}
                  className={`rounded-2xl border p-3 min-h-[200px] transition-colors ${isDragOver
                    ? "border-blue-400 bg-blue-50/50"
                    : "border-slate-200 bg-slate-100/60"
                    }`}
                >
                  {/* Column header */}
                  <div className="flex items-center justify-between px-1 mb-3">
                    <div className="flex items-center gap-2">
                      <span
                        className={`w-2 h-2 rounded-full ${columnConfig[statusKey].dot}`}
                      />
                      <span className="text-sm font-medium text-slate-700">
                        {columnConfig[statusKey].label}
                      </span>
                    </div>
                    <span className="text-xs font-medium text-slate-400 bg-white rounded-full px-2 py-0.5 border border-slate-200">
                      {columnTasks.length}
                    </span>
                  </div>

                  {/* Cards */}
                  <div className="flex flex-col gap-2.5">
                    {columnTasks.length === 0 && (
                      <div
                        className={`rounded-xl border border-dashed p-4 text-center text-xs ${isDragOver
                          ? "border-blue-300 text-blue-400"
                          : "border-slate-200 text-slate-400"
                          }`}
                      >
                        {isDragOver ? "Drop here" : "No tasks"}
                      </div>
                    )}

                    {columnTasks.map((task) => {
                      const projectName = getProjectName(task?.projectId);
                      const userName = getUserName(task?.assignedUserId);

                      return (
                        <div
                          key={task?.id}
                          draggable
                          onDragStart={() => handleDragStart(task?.id)}
                          onDragEnd={handleDragEnd}
                          className={`group bg-white rounded-xl border border-slate-200 p-3.5 cursor-grab active:cursor-grabbing hover:shadow-md transition ${draggedTaskId === task?.id
                            ? "opacity-40"
                            : "opacity-100"
                            }`}
                        >
                          {/* Task name + actions */}
                          <div className="flex items-start justify-between gap-2 mb-1.5">
                            <p className="text-sm font-medium text-slate-900 leading-snug">
                              {task?.taskName}
                            </p>
                            <div className="hidden group-hover:flex items-center gap-2 shrink-0">
                              <button
                                onClick={() => handleEditClick(task)}
                                className="text-xs text-blue-600 hover:underline"
                              >
                                Edit
                              </button>
                              <button
                                onClick={() => handleDelete(task?.id)}
                                disabled={deletingId === task?.id}
                                className="text-xs text-red-500 hover:underline disabled:opacity-50"
                              >
                                {deletingId === task?.id ? "..." : "Del"}
                              </button>
                            </div>
                          </div>

                          {/* Description */}
                          {task?.description && (
                            <p className="text-xs text-slate-500 line-clamp-2 mb-2.5">
                              {task?.description}
                            </p>
                          )}

                          {/* Project name badge */}
                          {projectName && (
                            <div className="mb-2">
                              <span className="text-[11px] font-medium px-2 py-0.5 rounded-full bg-purple-50 text-purple-600">
                                {projectName}
                              </span>
                            </div>
                          )}

                          {/* Priority + User avatar */}
                          <div className="flex items-center justify-between">
                            {task?.priority ? (
                              <span
                                className={`text-[11px] font-medium px-2 py-0.5 rounded-full ${priorityStyles[task?.priority] ||
                                  "bg-slate-100 text-slate-500"
                                  }`}
                              >
                                {task?.priority}
                              </span>
                            ) : (
                              <span className="text-[11px] text-slate-400">
                                {/* #{task?.id} */}
                              </span>
                            )}

                            {userName && (
                              <div
                                title={userName}
                                className="w-6 h-6 rounded-full bg-blue-100 text-blue-700 text-[11px] font-medium flex items-center justify-center"
                              >
                                {getInitials(userName)}
                              </div>
                            )}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};

export default Task;