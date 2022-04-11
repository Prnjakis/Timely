package com.example.timely.controller;

import com.example.timely.model.Project;
import com.example.timely.repository.ProjectRepository;
import com.example.timely.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;

@Controller
public class MainController {


	@Autowired
	ProjectService projectService;

	@Autowired
	ProjectRepository projectRepository;

	@GetMapping("/")
	public String home(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber, @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model)
	{
		model.addAttribute("projects", projectService.getProjectsPaged(pageNumber,size));
		if (projectService.getProjects().isEmpty())
		{
			model.addAttribute("completed",true);
			model.addAttribute("empty","true");
		}
		else
		{
			Project completed = projectService.getLast();
			model.addAttribute("completed",completed.getCompleted());
			model.addAttribute("empty","false");
		}
		model.addAttribute("name","");
		return "index";
	}


	@PostMapping("/Start")
	public String start(Model model)
	{
		projectService.setStartTime();

		if (projectService.getProjects().isEmpty())
		{
			model.addAttribute("completed",true);
		}
		else
		{
			model.addAttribute("completed",projectService.getLast().getCompleted());
		}

		model.addAttribute("projects", projectService.getProjectsPaged(1,10));
		model.addAttribute("name", "");
		return "redirect:/";

	}

	@PostMapping("/Stop")
	public String stop(Model model,@ModelAttribute("name")String name) throws ParseException
	{
		projectService.setStopTime(name);

		model.addAttribute("projects", projectService.getProjectsPaged(1,10));
		model.addAttribute("completed",true);

		return "redirect:/";
	}

	@GetMapping ("/delete/{id}")
	public String delete(@PathVariable Integer id)
	{
		projectService.deleteProjectById(id);
		return "redirect:/";
	}


	@GetMapping ("/edit/{id}")
	public String edit(@ModelAttribute("project")Project project)
	{
		projectService.updateProject(project);
		return "redirect:/";
	}

	@GetMapping("/deleteAll")
	public String deleteAll()
	{
		projectService.deleteAll();
		return "redirect:/";
	}

	@GetMapping("/export")
	public String export() throws IOException
	{
		projectService.export();
		return "redirect:/";
	}

}

