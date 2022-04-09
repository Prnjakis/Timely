package com.example.timely.controller;

import com.example.timely.model.Project;
import com.example.timely.repository.ProjectRepository;
import com.example.timely.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class MainController {

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	ProjectService projectService;

	@RequestMapping("/")
	public String home(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model) {
		model.addAttribute("projects", projectService.getProjects(pageNumber,size));
		if (projectRepository.findAll().isEmpty())
		{
			model.addAttribute("completed",true);
		}
		else
		{
			Project completed = projectRepository.findAll().get(projectRepository.findAll().size()-1);
			model.addAttribute("completed",completed.getCompleted());
		}
		model.addAttribute("name","");
		return "index";
	}


	@RequestMapping("/Start")
	public String start(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "size", required = false, defaultValue = "10") int size,Model model) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Project project = new Project();
		project.setStart(formatter.format(calendar.getTime()));

		projectRepository.save(project);
		if (projectRepository.findAll().isEmpty())
		{
			model.addAttribute("completed",true);
		}
		else
		{
			Project completed = projectRepository.findAll().get(projectRepository.findAll().size()-1);
			model.addAttribute("completed",completed.getCompleted());
		}
		model.addAttribute("projects", projectService.getProjects(pageNumber,size));
		model.addAttribute("name",new String());
		return "redirect:/";

	}

	@RequestMapping("/Stop")
	public String stop(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "size", required = false, defaultValue = "10") int size,Model model,@ModelAttribute("name")String name) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Project project = projectRepository.findAll().get(projectRepository.findAll().size()-1);
		project.setStop(formatter.format(calendar.getTime()));
		project.setCompleted(true);
		project.setName(name);
		String time1 = project.getStart();
		String time2 = project.getStop();

		Date date1 = formatter.parse(time1);
		Date date2 = formatter.parse(time2);
		long difference = date2.getTime() - date1.getTime();
		project.setDuration(Long.toString(difference));
		projectRepository.save(project);
		model.addAttribute("projects", projectService.getProjects(pageNumber,size));
		model.addAttribute("completed",true);
		return "redirect:/";
	}
}

