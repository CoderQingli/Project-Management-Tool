package com.qingli.projectmanager.services;

import com.qingli.projectmanager.domian.Backlog;
import com.qingli.projectmanager.domian.Project;
import com.qingli.projectmanager.domian.User;
import com.qingli.projectmanager.exceptions.ProjectIdException;
import com.qingli.projectmanager.exceptions.ProjectNotFoundException;
import com.qingli.projectmanager.repositories.BacklogRepository;
import com.qingli.projectmanager.repositories.ProjectRepository;
import com.qingli.projectmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username) {

        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if (existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: '" + project.getProjectIdentifier() + "' cannot be updated. It doesn't exist");
            }
        }

        try {
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if (project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            } else {
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }catch (Exception e) {
            throw new ProjectIdException("Project ID '" + project.getProjectIdentifier().toUpperCase() + "' already exist");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username) {

        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null) {
            throw new ProjectIdException("Project ID '" + projectId + "' not exist");
        }

        if (!project.getProjectLeader().equals(username)) {
            throw new ProjectNotFoundException("Project not found in your account");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String username) {
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }
}
