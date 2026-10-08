import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllProjects } from "../services/ProjectService";
import { getAllTasks } from "../services/TaskService";

const statusStyles = {
  TODO: "bg-slate-100 text-slate-600",
  PENDING: "bg-amber-50 text-amber-700",
  IN_PROGRESS: "bg-blue-50 text-blue-700",
  COMPLETED: "bg-green-50 text-green-700",
  DONE: "bg-green-50 text-green-700",
};

const SummaryCard = ({ label, value, icon, accent }) => (
  <div className="bg-white rounded-2xl border border-slate-200 p-5 flex items-center gap-4">
    <div className={`w-11 h-11 rounded-xl flex items-center justify-center ${accent.bg}`}>
      {icon}
    </div>
    <div>
      <p className="text-2xl font-medium text-slate-900">{value}</p>
      <p className="text-sm text-slate-500">{label}</p>
    </div>
  </div>
);

const ProjectIcon = ({ className }) => (
  <svg className={className} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z" />
  </svg>
);

const TaskIcon = ({ className }) => (
  <svg className={className} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
  </svg>
);

const CheckIcon = ({ className }) => (
  <svg className={className} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
  </svg>
);

const PendingIcon = ({ className }) => (
  <svg className={className} fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
  </svg>
);

const Dashboard = () => {
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [projectsRes, tasksRes] = await Promise.all([
        getAllProjects(),
        getAllTasks(),
      ]);
      setProjects(projectsRes.data.data);
setTasks(tasksRes.data.data);
    } catch (err) {
      console.error("Error loading dashboard data:", err);
      setError("Could not load dashboard data.");
    } finally {
      setLoading(false);
    }
  };

  const completedCount = tasks.filter(
    (t) => t.status === "COMPLETED" || t.status === "DONE"
  ).length;
  const pendingCount = tasks.length - completedCount;

  const getStatusClass = (status) =>
    statusStyles[status] || "bg-slate-100 text-slate-600";

  return (
    <div className="max-w-6xl mx-auto w-full px-4 py-8">

      <div className="mb-6">
        <h1 className="text-xl font-medium text-slate-900">Dashboard</h1>
        <p className="text-sm text-slate-500 mt-1">Overview of your projects and tasks</p>
      </div>

        {loading && (
          <div className="bg-white rounded-2xl border border-slate-200 p-10 flex flex-col items-center justify-center">
            <div className="w-6 h-6 border-2 border-slate-200 border-t-blue-600 rounded-full animate-spin mb-3"></div>
            <p className="text-sm text-slate-500">Loading dashboard...</p>
          </div>
        )}

        {!loading && error && (
          <div className="bg-red-50 border border-red-100 rounded-2xl p-6 text-center">
            <p className="text-sm text-red-600">{error}</p>
            <button
              onClick={loadData}
              className="mt-3 text-sm font-medium text-red-700 underline"
            >
              Try again
            </button>
          </div>
        )}

        {!loading && !error && (
          <>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
              <SummaryCard
                label="Total projects"
                value={projects.length}
                accent={{ bg: "bg-purple-50" }}
                icon={<ProjectIcon className="w-5 h-5 text-purple-600" />}
              />
              <SummaryCard
                label="Total tasks"
                value={tasks.length}
                accent={{ bg: "bg-blue-50" }}
                icon={<TaskIcon className="w-5 h-5 text-blue-600" />}
              />
              <SummaryCard
                label="Completed"
                value={completedCount}
                accent={{ bg: "bg-green-50" }}
                icon={<CheckIcon className="w-5 h-5 text-green-600" />}
              />
              <SummaryCard
                label="Pending"
                value={pendingCount}
                accent={{ bg: "bg-amber-50" }}
                icon={<PendingIcon className="w-5 h-5 text-amber-600" />}
              />
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">

              <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
                <div className="flex items-center justify-between px-5 py-4 border-b border-slate-100">
                  <h2 className="text-sm font-medium text-slate-900">Recent projects</h2>
                  <button
                    onClick={() => navigate("/projects")}
                    className="text-xs font-medium text-blue-600 hover:underline"
                  >
                    View all
                  </button>
                </div>
                {projects.length === 0 ? (
                  <p className="text-sm text-slate-500 px-5 py-6 text-center">No projects yet.</p>
                ) : (
                  <ul>
                    {projects.slice(0, 5).map((project) => (
                      <li
                        key={project.id}
                        className="px-5 py-3 border-b border-slate-50 last:border-b-0 hover:bg-slate-50 transition"
                      >
                        <p className="text-sm font-medium text-slate-900">{project.name}</p>
                        <p className="text-xs text-slate-500 mt-0.5 truncate">{project.description}</p>
                      </li>
                    ))}
                  </ul>
                )}
              </div>

              <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden">
                <div className="flex items-center justify-between px-5 py-4 border-b border-slate-100">
                  <h2 className="text-sm font-medium text-slate-900">Recent tasks</h2>
                  <button
                    onClick={() => navigate("/tasks")}
                    className="text-xs font-medium text-blue-600 hover:underline"
                  >
                    View all
                  </button>
                </div>
                {tasks.length === 0 ? (
                  <p className="text-sm text-slate-500 px-5 py-6 text-center">No tasks yet.</p>
                ) : (
                  <ul>
                    {tasks.slice(0, 5).map((task) => (
                      <li
                        key={task.id}
                        className="px-5 py-3 border-b border-slate-50 last:border-b-0 hover:bg-slate-50 transition flex items-center justify-between gap-3"
                      >
                        <div className="min-w-0">
                          <p className="text-sm font-medium text-slate-900 truncate">{task.taskName}</p>
                          <p className="text-xs text-slate-500 mt-0.5 truncate">{task.description}</p>
                        </div>
                        <span className={`shrink-0 inline-block text-xs font-medium px-2.5 py-1 rounded-full ${getStatusClass(task.status)}`}>
                          {task.status}
                        </span>
                      </li>
                    ))}
                  </ul>
                )}
              </div>

            </div>
          </>
        )}

      
    </div>
  );
};

export default Dashboard;